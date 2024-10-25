package com.example.sociallogintest.member.repository;

import com.example.sociallogintest.member.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

//    @Modifying
//    @Query("DELETE FROM Member m WHERE m.id = :id")
//    void deleteById(@Param("id") String id);

//    @Query("select m from Member m where m.id = :id")
//    Optional<Member> selectById(@Param("id") String id);

    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select m from Member m where m.fromSocial = :social and m.email =:email")
    Optional<Member> findByEmail(String email, boolean social);

    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select m from Member m where m.fromSocial = :social and m.email =:email")
    Optional<Member> findByEmail(String email);

}
