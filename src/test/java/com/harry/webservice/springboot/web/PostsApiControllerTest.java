package com.harry.webservice.springboot.web;

import com.harry.webservice.springboot.domain.posts.Posts;
import com.harry.webservice.springboot.domain.posts.PostsRepository;
import com.harry.webservice.springboot.web.dto.PostsSaveRequestDto;
import com.harry.webservice.springboot.web.dto.PostsUpdateRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @After
    void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    void Posts_등록_된다() throws Exception {
        // given
        final String title = "The title.";
        final String content = "blah blah blah...";
        final String author = "Harry";
        final PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
        final String url = "http://localhost:" + port + "/api/v1/posts";

        // when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> postsList = postsRepository.findAll();
        assertThat(postsList.get(0).getTitle()).isEqualTo(title);
        assertThat(postsList.get(0).getContent()).isEqualTo(content);
        assertThat(postsList.get(0).getAuthor()).isEqualTo(author);
    }

    @Test
    void Posts_수정_된다() throws Exception {
        // given
        Posts savePosts = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build()
        );

        final Long updateId = savePosts.getId();
        final String expectedTitle = "title2";
        final String expectedContent = "content2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        final String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;
        final HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        // when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> postsList = postsRepository.findAll();

        assertThat(postsList.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(postsList.get(0).getContent()).isEqualTo(expectedContent);
    }

    @Test
    void BaseTimeEntity_등록() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 11, 28, 0, 0, 0);
        postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build()
        );

        // when
        List<Posts> postsList = postsRepository.findAll();

        // then
        System.out.println(">>>>>>>>>>>>>>>> createDate : " + postsList.get(0).getCreatedDate() + ", modifiedDate : " + postsList.get(0).getModifiedDate());

        assertThat(postsList.get(0).getCreatedDate()).isAfter(now);
        assertThat(postsList.get(0).getModifiedDate()).isAfter(now);
    }

}