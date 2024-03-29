package myblogserver.service;

import myblogserver.entity.Article;
import myblogserver.exception.XException;
import myblogserver.repository.ArticleRepository;
import myblogserver.utils.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public Mono<Article> addArticle(Article article) {
        Mono<Article> articleM = articleRepository.findFirstByLabel(article.getLabel());
       return articleRepository.findCountByLabel(article.getLabel())
               .filter(r -> r != 0)
               .flatMap(r -> articleM.flatMap(a -> articleRepository.updateLabelCountByLabel(article.getLabel(), a.getLabelCount()+1)))
               .flatMap(r -> articleM.flatMap(a -> {
                   article.setLabelCount(a.getLabelCount());
                   return articleRepository.save(article);
               }))
               .switchIfEmpty(articleRepository.save(article));
    }

    public Mono<List<Article>> listArticles(int page, int pageSize) {
        return articleRepository.findAll((page-1)*pageSize, pageSize).collectList();
    }

    public Mono<Integer> listArticlesCount() {
        return articleRepository.findCount();
    }

    public Mono<Void> resetArticle(Article article) {
        return articleRepository.save(article)
                .then();
    }

    public Mono<Article> getArticleById(long aid) {
        return articleRepository.findById(aid);
    }

    public Mono<List<Article>> getArticleByLabel(String label) {
        return articleRepository.findByLabel(label).collectList();
    }

    @Transactional
    public Mono<Void> deleteArticle(long aid) {
        Mono<Article> articleM = articleRepository.findById(aid);
        return articleM.flatMap(article -> articleRepository.updateLabelCountByLabel(
                article.getLabel(), article.getLabelCount()-1
        )).then(articleRepository.deleteById(aid).then());
    }

    public Mono<List<Article>> listLabelsAndCount() {
        return articleRepository.findLabelsAndCount().collectList();
    }
}
