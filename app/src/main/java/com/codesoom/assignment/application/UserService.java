// 1. createUser -> 회원 생성
// 2. updateUser -> 회원 수정
// 3. deleteUser -> 회원 삭제

package com.codesoom.assignment.application;

import com.codesoom.assignment.UserNotFoundException;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserData;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;

/**
 * 회원을 생성, 수정, 삭제하는 service 입니다.
 * */
@Service
public class UserService {
    private final Mapper mapper;
    private final UserRepository userRepository;

    public UserService(Mapper dozerMapper, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.mapper = dozerMapper;
    }

    /**
     * userData를 받아 새로운 user를 생성하여 리턴합니다.
     *
     * @param userData 회원 정보
     * @return 새로 생성된 회원
     * */
    public User create(UserData userData) {
        User user = mapper.map(userData, User.class);
        return userRepository.save(user);
    }

    /**
     * 기존 회원 목록 중 id가 일치하는 회원을 찾아 userData 회원 정보로 수정하여 변경된 회원을 리턴합니다.
     *
     * @param id 회원 id
     * @param userData 변경하려는 정보
     * @return 변경된 회원
     * */
    public User update(Long id, UserData userData) {
        User user = findUser(id);

        user.change(
                userData.getName(),
                userData.getEmail(),
                userData.getPassword()
        );

        return user;
    }

    /**
     * 기존 회원 목록 중 id가 일치하는 회원을 찾아 회원을 삭제하고 삭제된 회원을 리턴합니다.
     *
     * @param id 회원 id
     * @return 삭제된 회원
     * */
    public User delete(Long id) {
        User user = findUser(id);

        userRepository.delete(user);

        return user;
    }

    /**
     * id를 받아서 일치하는 회원을 찾으면 회원을 리턴하고, 못찾았으면 예외를 던집니다.
     *
     * @param id 회원 id
     * @throws UserNotFoundException id가 일치하는 회원이 없는 경우
     * */
    public User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
