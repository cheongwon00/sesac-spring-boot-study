package com.example.personal_project.application;

import com.example.personal_project.domain.CollectionItem;
import com.example.personal_project.domain.CollectionItemRepository;
import com.example.personal_project.presentation.CollectionItemDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CollectionItemServiceTest {

    @Mock
    private CollectionItemRepository collectionItemRepository;

    @Mock
    private ValidationService validationService;

    private CollectionItemService collectionItemService;

    @BeforeEach
    void setUp() {
        collectionItemService = new CollectionItemService(collectionItemRepository, validationService);
    }

    @Test
    @DisplayName("아이템을 추가할 때 이름 양끝의 공백을 제거하고 저장 결과를 반환한다")
    void addTrimsNameAndReturnsSavedItem() {
        CollectionItemDto request = new CollectionItemDto(null, "  키보드  ", 50_000);
        when(collectionItemRepository.add(org.mockito.ArgumentMatchers.any(CollectionItem.class)))
                .thenAnswer(invocation -> {
                    CollectionItem item = invocation.getArgument(0);
                    item.setId(1L);
                    return item;
                });

        CollectionItemDto result = collectionItemService.add(request);

        ArgumentCaptor<CollectionItem> itemCaptor = ArgumentCaptor.forClass(CollectionItem.class);
        verify(validationService).checkValid(itemCaptor.capture());
        verify(collectionItemRepository).add(itemCaptor.getValue());

        assertThat(itemCaptor.getValue().getName()).isEqualTo("키보드");
        assertThat(result).isEqualTo(new CollectionItemDto(1L, "키보드", 50_000));
    }

    @Test
    @DisplayName("전체 아이템을 DTO 목록으로 변환한다")
    void findAllMapsItemsToDtos() {
        when(collectionItemRepository.findAll()).thenReturn(List.of(
                item(1L, "키보드", 50_000),
                item(2L, "마우스", 30_000)
        ));

        List<CollectionItemDto> result = collectionItemService.findAll();

        assertThat(result).containsExactly(
                new CollectionItemDto(1L, "키보드", 50_000),
                new CollectionItemDto(2L, "마우스", 30_000)
        );
    }

    @Test
    @DisplayName("예산 이하의 아이템 중 가장 저렴한 세 개를 추천한다")
    void findItemsWithBudgetReturnsThreeCheapestItems() {
        when(collectionItemRepository.findAll()).thenReturn(List.of(
                item(1L, "모니터", 200_000),
                item(2L, "키보드", 50_000),
                item(3L, "마우스", 30_000),
                item(4L, "장패드", 10_000),
                item(5L, "웹캠", 70_000)
        ));

        List<CollectionItemDto> result = collectionItemService.findItemsWithBudget(100_000);

        assertThat(result).extracting(CollectionItemDto::name)
                .containsExactly("장패드", "마우스", "키보드");
        assertThat(result).extracting(CollectionItemDto::price)
                .containsExactly(10_000, 30_000, 50_000);
    }

    private CollectionItem item(Long id, String name, Integer price) {
        CollectionItem item = new CollectionItem(name, price);
        item.setId(id);
        return item;
    }
}
