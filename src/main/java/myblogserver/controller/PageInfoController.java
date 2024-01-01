package myblogserver.controller;

import myblogserver.entity.PageInfo;
import myblogserver.exception.XException;
import myblogserver.service.PageInfoService;
import myblogserver.utils.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/pageInfo")
@CrossOrigin
public class PageInfoController {

    @Autowired
    private PageInfoService pageInfoService;

    /**
     * 获取页面列表信息
     * @return
     */
    @GetMapping("/info")
    public Mono<ResultVO> getPages() {
        return pageInfoService.listPages()
                .flatMap(pages -> Mono.just(ResultVO.success(Map.of("pages", pages))));
    }

    /**
     * 修改页面信息
     * @param pageInfo
     * @return
     */
    @PostMapping("/info")
    public Mono<ResultVO> putPageInfo(@RequestBody PageInfo pageInfo) {
        return pageInfoService.resetPageInfo(pageInfo)
                .then(Mono.just(ResultVO.success("修改成功")));
    }

    /**
     * 获取页面信息
     * @param name
     * @return
     */
    @GetMapping("/info/{name}")
    public Mono<ResultVO> getPageInfo(@PathVariable String name) {
        return pageInfoService.getPageInfoByName(name)
                .map(pageInfo -> ResultVO.success(Map.of("pageInfo", pageInfo)));
    }
}
