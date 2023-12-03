package com.harry.webservice.springboot.web;

import com.harry.webservice.springboot.domain.posts.Posts;
import com.harry.webservice.springboot.domain.posts.PostsRepository;
import com.harry.webservice.springboot.web.dto.PostsSaveRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Rollback
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IndexControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void 메인_페이지_로딩() {
        // when
        final String body = restTemplate.getForObject("/", String.class);

        // then
        assertThat(body).contains("게시글 번호", "제목", "작성자", "최종 수정일");
    }

    @Test
    void 게시물_등록_페이지_로딩() {
        // when
        final String body = restTemplate.getForObject("/posts/save", String.class);

        // then
        assertThat(body).contains("제목을 입력하세요.", "작성자를 입력하세요.", "내용을 입력하세요.");
    }

    @Test
    void 게시물_수정_페이지_로딩() {
        // given
        final Long id = 1L;
        final String title = "The title.";
        final String content = "blah blah blah...";
        final String author = "Harry";
        final PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
        final String url = "http://localhost:" + port + "/api/v1/posts";

        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> postsList = postsRepository.findAll();
        assertThat(postsList.get(0).getTitle()).isEqualTo(title);
        assertThat(postsList.get(0).getContent()).isEqualTo(content);
        assertThat(postsList.get(0).getAuthor()).isEqualTo(author);

        // when
        final String body = restTemplate.getForObject("/posts/update/" + id, String.class, id);

        // then
        assertThat(body).contains("제목", "내용");
    }

}
