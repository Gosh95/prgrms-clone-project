package prgrms.project.starbooks.domain.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @ParameterizedTest
    @MethodSource("sampleForTestInvalidEmail")
    @DisplayName("이메일 주소가 형식에 맞지 않을 경우 예외가 발생해야 한다.")
    void testInvalidEmail(String input) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Email(input)
        );
    }

    static List<String> sampleForTestInvalidEmail() {
        return List.of(
                "bbbb",
                "",
                """
                aaaaaaaaaa
                aaaaaaaaaa
                aaaaaaaaaa
                aaaaaaaaaa
                aaaaaaaaaa
                @gmail.com
                """
        );
    }


    @ParameterizedTest
    @MethodSource("sampleForTestValidEmail")
    @DisplayName("이메일 주소가 형식에 맞을 경우 생성이 되어야 한다.")
    void testValidEmail(String input) {
        var email = new Email(input);

        assertNotNull(email);
        assertEquals(email.address(), input);
    }

    static List<String> sampleForTestValidEmail() {
        return List.of(
                "test@gmail.com",
                "test123@naver.com",
                "asdfasdf123@github.com"
        );
    }
}