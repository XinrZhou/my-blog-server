package myblogserver.service;


import myblogserver.entity.PageInfo;
import myblogserver.exception.XException;
import myblogserver.repository.PageInfoRepository;
import myblogserver.utils.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class PageInfoService {

    @Autowired
    private PageInfoRepository pageInfoRepository;

    public Mono<PageInfo> getPageInfoByName(String name) {
        return pageInfoRepository.findByName(name);
    }

    public Mono<List<Map<String, String>>> listPages() {
        return pageInfoRepository.findAll()
                .map(pageInfo -> Map.of("name", pageInfo.getName()))
                .collectList();
    }

    public Mono<PageInfo> resetPageInfo(PageInfo pageInfo) {
        return pageInfoRepository.findByName(pageInfo.getName())
                .filter(Objects::nonNull)
                .flatMap(pageInfoM -> {
                    pageInfo.setId(pageInfoM.getId());
                    pageInfo.setUpdateTime(LocalDateTime.now());
                    return pageInfoRepository.save(pageInfo);
                })
                .switchIfEmpty(pageInfoRepository.save(pageInfo));
    }
}
