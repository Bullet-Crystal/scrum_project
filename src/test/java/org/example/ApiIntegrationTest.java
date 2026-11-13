package org.example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullScrumFlow_andProjectIsolation() throws Exception {
        String owner = "owner-" + UUID.randomUUID().toString().substring(0, 8);
        String other = "other-" + UUID.randomUUID().toString().substring(0, 8);
        String ownerToken = register(owner);
        String otherToken = register(other);

        JsonNode project = postJson("/api/v1/projects", ownerToken,
                Map.of("title", "Scrum project", "description", "Integration test"));
        long projectId = project.get("id").asLong();
        long backlogId = project.get("productBacklog").get("id").asLong();

        JsonNode story = postJson("/api/v1/product-backlogs/" + backlogId + "/user-stories", ownerToken,
                Map.of(
                        "title", "Login",
                        "role", "user",
                        "action", "log in",
                        "goal", "access my data",
                        "priority", "MUST_HAVE",
                        "acceptanceCriteria", "valid credentials"));
        long storyId = story.get("id").asLong();
        assertThat(story.get("status").asText()).isEqualTo("TO_DO");

        JsonNode sprint = postJson("/api/v1/projects/" + projectId + "/sprints", ownerToken,
                Map.of(
                        "name", "Sprint 1",
                        "startDate", LocalDate.now().toString(),
                        "endDate", LocalDate.now().plusDays(14).toString()));
        long sprintId = sprint.get("id").asLong();

        mockMvc.perform(post("/api/v1/sprints/" + sprintId + "/user-stories")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("userStoryId", storyId))))
                .andExpect(status().isNoContent());

        JsonNode task = postJson("/api/v1/sprints/" + sprintId + "/tasks", ownerToken,
                Map.of("userStoryId", storyId, "title", "Implement login", "description", "backend"));
        long taskId = task.get("id").asLong();
        assertThat(task.get("status").asText()).isEqualTo("TO_DO");

        mockMvc.perform(patch("/api/v1/tasks/" + taskId + "/status")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("status", "DONE"))))
                .andExpect(status().isOk());

        MvcResult sprintResult = mockMvc.perform(get("/api/v1/sprints/" + sprintId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ownerToken))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode loadedSprint = objectMapper.readTree(sprintResult.getResponse().getContentAsString());
        assertThat(loadedSprint.get("userStories")).hasSize(1);
        assertThat(loadedSprint.get("tasks")).hasSize(1);
        assertThat(loadedSprint.get("tasks").get(0).get("status").asText()).isEqualTo("DONE");

        // A second user cannot see or access the first user's project.
        MvcResult projectsResult = mockMvc.perform(get("/api/v1/projects")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + otherToken))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(objectMapper.readTree(projectsResult.getResponse().getContentAsString())).isEmpty();

        mockMvc.perform(get("/api/v1/projects/" + projectId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + otherToken))
                .andExpect(status().isForbidden());
    }

    private String register(String username) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "username", username,
                                "password", "password123"))))
                .andExpect(status().isCreated())
                .andReturn();
        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        assertThat(body.get("roles").toString()).contains("DEVELOPER");
        return body.get("token").asText();
    }

    private JsonNode postJson(String url, String token, Object body) throws Exception {
        MvcResult result = mockMvc.perform(post(url)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andReturn();
        assertThat(result.getResponse().getStatus())
                .as("Response body: " + result.getResponse().getContentAsString())
                .isBetween(200, 299);
        return objectMapper.readTree(result.getResponse().getContentAsString());
    }
}
