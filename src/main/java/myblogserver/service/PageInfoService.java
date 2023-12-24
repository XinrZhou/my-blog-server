package myblogserver.service;


import myblogserver.entity.PageInfo;
import myblogserver.exception.XException;
import myblogserver.repository.PageInfoRepository;
import myblogserver.utils.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class PageInfoService {

    @Autowired
    private PageInfoRepository pageInfoRepository;

    public Mono<PageInfo> getPageInfoByName(String name) {
        return pageInfoRepository.findByName(name);
    }

    public Mono<List<PageInfo>> getPageInfo() {
        return pageInfoRepository.findAll().collectList();
    }

    public Mono<Void> resetPageInfo(PageInfo pageInfo) {
        return pageInfoRepository.findByName(pageInfo.getName())
                .flatMap(pageInfoM -> {
                    // 该页无背景图片等信息，向数据库中插入一条记录
                    if (pageInfoM == null ) {
                        return pageInfoRepository.save(pageInfo);
                    } else {
                        return pageInfoRepository.updatePageInfoByName(
                                pageInfoM.getName(),
                                pageInfoM.getBackgroundUrl(),
                                pageInfoM.getDescription()
                        );
                    }
                })
                .then();
    }
}
