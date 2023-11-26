package com.harry.webservice.springboot.domain.posts;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class PostsRepositoryTest {

    @Autowired
    public PostsRepository postsRepository;

    @After
    void cleanUp() {
        postsRepository.deleteAll();
    }

    @Test
    void 게시글_저장_불러오기() {
        // given
        final String title = "테스트 게시글";
        final String content = "테스트 본문";
        final String author = "Harry";

        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .build()
        );

        // when
        List<Posts> postsList = postsRepository.findAll();

        // then
        postsList.forEach(posts -> {
            assertThat(posts.getTitle()).isEqualTo(title);
            assertThat(posts.getContent()).isEqualTo(content);
            assertThat(posts.getAuthor()).isEqualTo(author);
        });
    }

}