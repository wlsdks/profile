package com.jinan.profile.service.pagination;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class PaginationService {

    private static final int BAR_LENGTH = 5;

    public List<Integer> getPaginationBarNumbers(int currentPageNumber, int totalPages) {

        // 중앙값을 찾아가기 위해 currentPageNumber에서 총길이/2를 뺀다. 0이랑 비교해서 더 큰수를 사용한다. -> 음수방지
        int startNumber = Math.max(currentPageNumber - (BAR_LENGTH / 2), 0);
        // min을 사용해서 두개중 작은 수를 사용한다.
        int endNumber = Math.min(startNumber + BAR_LENGTH, totalPages);
        // stream으로 boxing해서 데이터를 내보낸다.
        return IntStream.range(startNumber, endNumber).boxed().toList();
    }

    public int currentBarLength() {
        return BAR_LENGTH;
    }

}
