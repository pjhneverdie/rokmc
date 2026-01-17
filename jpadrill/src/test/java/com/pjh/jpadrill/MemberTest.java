package com.pjh.jpadrill;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.pjh.jpadrill.member.entity.Member;
import com.pjh.jpadrill.member.enumtype.MemberRole;
import com.pjh.jpadrill.member.vo.Address;
import com.pjh.jpadrill.project.entity.Project;
import com.pjh.jpadrill.project.entity.ProjectMember;
import com.pjh.jpadrill.project.enumtype.ProjectStatus;
import com.pjh.jpadrill.project.vo.ProjectPeriod;

@CustomDataJpaTest
public class MemberTest {

    @Autowired
    private TestEntityManager em;

    @Test
    public void createMember() {
        Address address = Address.createAddress("city", "street", "zipcode");

        Member member = Member.createMember("123@naver.com", "name",
                MemberRole.TESTER, address);

        Member savedMember = em.persist(member);

        em.flush();

        LocalDateTime dd = LocalDateTime.now();

        Project project = Project.createProject("null", "null",
                ProjectStatus.IN_PROGRESS,
                new ProjectPeriod(dd, dd.plusDays(2)));

        em.persist(project);

        em.flush();

        ProjectMember projectMember = ProjectMember.createProjectMember(project, savedMember, "dsadsasda");

        em.persist(projectMember);

        em.flush();
        em.clear();

        Project foundProject = em.find(Project.class, project.getId());
        System.out.println("결과: " + foundProject.getProjectMembers().get(0).getMember().getEmail());

    }

}
