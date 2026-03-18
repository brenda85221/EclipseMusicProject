package team5.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team5.forum.model.ForumType;

import java.util.List;

@Service
@Transactional
public class ForumTypeService {

    @Autowired
    private ForumTypeRepository forumTypeRepo;

    public List<ForumType> getAllForumTypes() {
        return forumTypeRepo.findAll();
    }

    public ForumType getForumTypeById(int id) {
        return forumTypeRepo.findById(id).orElse(null);
    }

    public ForumType createForumType(ForumType forumType) {
        return forumTypeRepo.save(forumType);
    }

    public ForumType updateForumType(ForumType forumType) {
        return forumTypeRepo.save(forumType);
    }

    public void deleteForumTypeById(int id) {
        forumTypeRepo.deleteById(id);
    }

    public boolean existsByForumTypeName(String forumTypeName) {
        ForumType byForumTypeName = forumTypeRepo.findByForumTypeName(forumTypeName);
        return byForumTypeName != null;
    }

}
