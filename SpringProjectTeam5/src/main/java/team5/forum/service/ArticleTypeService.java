package team5.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team5.forum.model.ArticleType;
import team5.forum.model.ForumType;

import java.util.List;

@Service
@Transactional
public class ArticleTypeService {

    @Autowired
    private ArticleTypeRepository articleTypeRepo;

    public List<ArticleType> getAllArticleTypes() {
        return articleTypeRepo.findAll();
    }

    public ArticleType getArticleTypeById(Integer id) {
        return articleTypeRepo.findById(id).orElse(null);
    }

    public ArticleType createArticleType(ArticleType articleType) {
        return articleTypeRepo.save(articleType);
    }

    public ArticleType updateArticleType(ArticleType articleType) {
        return articleTypeRepo.save(articleType);
    }

    public void deleteArticleTypeById(Integer id) {
        articleTypeRepo.deleteById(id);
    }

    public boolean existsByArticleTypeName(String articleTypeName) {
        ArticleType byArticleTypeName = articleTypeRepo.findByArticleTypeName(articleTypeName);
        return byArticleTypeName != null;
    }



}
