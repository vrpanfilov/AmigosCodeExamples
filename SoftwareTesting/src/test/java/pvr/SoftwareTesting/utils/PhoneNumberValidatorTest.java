package pvr.SoftwareTesting.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PhoneNumberValidatorTest {

    private PhoneNumberValidator underTest;

    @BeforeEach
    void setUp() {
        underTest = new PhoneNumberValidator();
    }

    @ParameterizedTest
    @CsvSource({
            "+447000000000, true",
            "+4470000000008878, false",
            "447000000000, false"
    })
    void itShouldValidatePhoneNumber(String phoneNumber, boolean expected) {
        // When
        boolean isValid = underTest.test(phoneNumber);

        // Then
        assertThat(isValid).isEqualTo(expected);
    }

    @Test
    void itShouldValidatePhoneNumber() {
        // Given
        String phoneNumber = "+447000000000";

        // When
        boolean isValid = underTest.test(phoneNumber);

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Should fail when length is bigger than 13")
    void itShouldValidatePhoneNumberWhenIncorrectAndHasLengthBiggerThan13() {
        // Given
        String phoneNumber = "+4470000000008878";

        // When
        boolean isValid = underTest.test(phoneNumber);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should fail when does not start with +")
    void itShouldValidatePhoneNumberWhenDoesNotStartWithPlusSign() {
        // Given
        String phoneNumber = "447000000000";

        // When
        boolean isValid = underTest.test(phoneNumber);

        // Then
        assertThat(isValid).isFalse();
    }
}
