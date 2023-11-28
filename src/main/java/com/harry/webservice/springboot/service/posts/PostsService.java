package com.harry.webservice.springboot.service.posts;

import com.harry.webservice.springboot.domain.posts.Posts;
import com.harry.webservice.springboot.domain.posts.PostsRepository;
import com.harry.webservice.springboot.web.dto.PostsListResponseDto;
import com.harry.webservice.springboot.web.dto.PostsResponseDto;
import com.harry.webservice.springboot.web.dto.PostsSaveRequestDto;
import com.harry.webservice.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id = " + id + "번 게시글이 없습니다."));

        entity.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id = " + id + "번 게시글이 없습니다."));

        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

}
