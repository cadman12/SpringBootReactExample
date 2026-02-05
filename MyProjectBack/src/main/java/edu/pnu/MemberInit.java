package edu.pnu;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import edu.pnu.domain.Member;
import edu.pnu.domain.Role;
import edu.pnu.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberInit implements ApplicationRunner {
	private final MemberRepository memRepo;
	private final PasswordEncoder encoder;
	@Override
	public void run(ApplicationArguments args) throws Exception {
		memRepo.save(Member.builder().username("member").role(Role.ROLE_MEMBER)
			.password(encoder.encode("abcd"))	.enabled(true).build());
		memRepo.save(Member.builder().username("manager").role(Role.ROLE_MANAGER)
			.password(encoder.encode("abcd"))	.enabled(true).build());
		memRepo.save(Member.builder().username("admin").role(Role.ROLE_ADMIN)
			.password(encoder.encode("abcd")).enabled(true).build());
	}

}
