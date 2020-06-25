package com.fsl.web.controller.forms;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenForm {
    private String refreshToken;
}
