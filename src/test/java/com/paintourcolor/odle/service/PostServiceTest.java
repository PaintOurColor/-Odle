package com.paintourcolor.odle.service;

import com.paintourcolor.odle.dto.post.request.PostCreateRequest;
import com.paintourcolor.odle.entity.*;
import com.paintourcolor.odle.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // JUnit과 Mockito를 연결해주는 어노테이션
class PostServiceTest {
//    @Test
//    void createPost() {
//    }
//
//    @Test
//    void getPostList() {
//    }
//
//    @Test
//    void getPost() {
//    }
//
//    @Test
//    void updatePost() {
//    }
//
//    @Test
//    void deletePost() {
//    }
    // 가짜 객체 생성
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MusicRepository musicRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private PostTagRepository postTagRepository;

    @InjectMocks // 테스트 할 대상
    private PostService postService;

    @Test
    @DisplayName("게시글 작성_노래가 있을 경우")
    void createPost_existMusic() {
        // given
        Music music = new Music(223L, "네 생각에 잠겨 (with 윤)", "정지우", "https://cdnimg.melon.co.kr/cm/album/images/102/26/907/10226907_500.jpg/melon/resize/120/quality/80/optimize");
        when(musicRepository.findMusicByMelonKoreaId(any())).thenReturn(music);

        PostCreateRequest request = PostCreateRequest.builder()
                .melonId(223L)
                .title("네 생각에 잠겨 (with 윤)")
                .singer("정지우")
                .cover("https://cdnimg.melon.co.kr/cm/album/images/102/26/907/10226907_500.jpg/melon/resize/120/quality/80/optimize")
                .content("내용")
                .openOrEnd(OpenOrEndEnum.OPEN)
                .emotion(EmotionEnum.HAPPY)
                .tagList("#태그").build();

        User user = new User();

        // when
        postService.createPost(request, user);

        // then
        assertThat(music.getHappyCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시글 작성_노래가 없을 경우")
    void createPost_noExistMusic() {
        // given
        when(musicRepository.findMusicByMelonKoreaId(any())).thenReturn(null);

        PostCreateRequest request = PostCreateRequest.builder()
                .melonId(10L)
                .title("제목")
                .singer("가수")
                .cover("커버")
                .content("내용")
                .openOrEnd(OpenOrEndEnum.OPEN)
                .emotion(EmotionEnum.HAPPY)
                .tagList("#태그").build();

        User user = new User();

        // when
        postService.createPost(request, user);

        // then
        Music music = new Music(request.getMelonId(), request.getTitle(), request.getSinger(), request.getCover());
        music.plusEmotionCount(request.getEmotion());
        verify(musicRepository).save(any(Music.class));
    }

    @Test
    @DisplayName("게시글 작성_태그가 있을 경우")
    void createPost_existTag() {
        // given
        Music music = new Music(223L, "네 생각에 잠겨 (with 윤)", "정지우", "https://cdnimg.melon.co.kr/cm/album/images/102/26/907/10226907_500.jpg/melon/resize/120/quality/80/optimize");
        when(musicRepository.findMusicByMelonKoreaId(any())).thenReturn(music);

        PostCreateRequest request = PostCreateRequest.builder()
                .melonId(223L)
                .title("네 생각에 잠겨 (with 윤)")
                .singer("정지우")
                .cover("https://cdnimg.melon.co.kr/cm/album/images/102/26/907/10226907_500.jpg/melon/resize/120/quality/80/optimize")
                .content("내용")
                .openOrEnd(OpenOrEndEnum.OPEN)
                .emotion(EmotionEnum.HAPPY)
                .tagList("#tag1 #tag2").build();
        User user = new User();

        Tag tag1 = new Tag("#tag1");
        Tag tag2 = new Tag("#tag2");
        when(tagRepository.findByTagName("#tag1")).thenReturn(tag1);
        when(tagRepository.findByTagName("#tag2")).thenReturn(tag2);

        // when
        postService.createPost(request, user);

        // then
        Post post = new Post(user, music, request.getContent(), request.getOpenOrEnd(), request.getEmotion());
        PostTag postTag1 = new PostTag(post, tag1);
        PostTag postTag2 = new PostTag(post, tag2);
        verify(postTagRepository, times(2)).save(any(PostTag.class));
    }

    @Test
    @DisplayName("게시글 작성_태그가 없을 경우")
    void createPost_noExistTag() {
        // given
        Music music = new Music(223L, "네 생각에 잠겨 (with 윤)", "정지우", "https://cdnimg.melon.co.kr/cm/album/images/102/26/907/10226907_500.jpg/melon/resize/120/quality/80/optimize");
        when(musicRepository.findMusicByMelonKoreaId(any())).thenReturn(music);

        PostCreateRequest request = PostCreateRequest.builder()
                .melonId(223L)
                .title("네 생각에 잠겨 (with 윤)")
                .singer("정지우")
                .cover("https://cdnimg.melon.co.kr/cm/album/images/102/26/907/10226907_500.jpg/melon/resize/120/quality/80/optimize")
                .content("내용")
                .openOrEnd(OpenOrEndEnum.OPEN)
                .emotion(EmotionEnum.HAPPY)
                .tagList("").build();
        User user = new User();

        // when
        postService.createPost(request, user);

        // then
        assertThat(postTagRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("게시글 작성_게시글 생성 완료")
    void createPost_success() {
        // given
        Music music = new Music(223L, "네 생각에 잠겨 (with 윤)", "정지우", "https://cdnimg.melon.co.kr/cm/album/images/102/26/907/10226907_500.jpg/melon/resize/120/quality/80/optimize");
        when(musicRepository.findMusicByMelonKoreaId(any())).thenReturn(music);

        PostCreateRequest request = PostCreateRequest.builder()
                .melonId(223L)
                .title("네 생각에 잠겨 (with 윤)")
                .singer("정지우")
                .cover("https://cdnimg.melon.co.kr/cm/album/images/102/26/907/10226907_500.jpg/melon/resize/120/quality/80/optimize")
                .content("내용")
                .openOrEnd(OpenOrEndEnum.OPEN)
                .emotion(EmotionEnum.HAPPY)
                .tagList("").build();
        User user = new User();

        // when
        postService.createPost(request, user);

        // then
        verify(postRepository).save(any(Post.class));
    }

    @Test
    @DisplayName("게시글 전체 조회")
    void getPostList_wj() {
        // given
        User user = new User();
        Music music = new Music();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Post post1 = new Post(user, music, "content1", OpenOrEndEnum.OPEN, EmotionEnum.ANGRY);
        Post post2 = new Post(user, music, "content2", OpenOrEndEnum.END, EmotionEnum.SAD);
        List<Post> posts = Arrays.asList(post1, post2);
        when(postRepository.findAllByOrderByCreatedAtDesc(pageable)).thenReturn(new PageImpl<>(posts));

        // when
        postService.getPostList(pageable);

        // then
        verify(postRepository, times(1)).findAllByOrderByCreatedAtDesc(pageable);
    }

    @Test
    @DisplayName("게시글 개별 조회")
    void getPost_existPost() {
        // given
        User user = new User();
        Music music = new Music();
        Post post = new Post(user, music, "content1", OpenOrEndEnum.OPEN, EmotionEnum.ANGRY);
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(postTagRepository.findTagIdByPostId(post.getId())).thenReturn(List.of(new PostTag(post, new Tag("#tag1")), new PostTag(post, new Tag("#tag2")), new PostTag(post, new Tag("#tag3"))));

        // when
        postService.getPost(post.getId());

        // then
        verify(postRepository).findById(post.getId());
        verify(postTagRepository).findTagIdByPostId(post.getId());
    }

    @Test
    @DisplayName("게시글 개별 조회_게시글이 없을 경우")
    void getPost_noExistPost() {
        // given
        Long postId = 10L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // when
        // then
        assertThrows(NoSuchElementException.class, () -> postService.getPost(postId));
        verify(postRepository).findById(postId);
    }

}