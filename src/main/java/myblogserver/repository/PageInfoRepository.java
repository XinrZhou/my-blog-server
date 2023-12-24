package myblogserver.repository;

import myblogserver.entity.PageInfo;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PageInfoRepository extends ReactiveCrudRepository<PageInfo, Long> {

    Mono<PageInfo> findByName(String name);

    @Modifying
    @Query("update pageInfo p set p.backgroundUrl=:backgroundUrl, p.description=:description where p.name=:name")
    Mono<Integer> updatePageInfoByName(String name, String backgroundUrl, String description);
}
