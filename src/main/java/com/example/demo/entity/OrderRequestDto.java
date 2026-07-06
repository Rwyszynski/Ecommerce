package com.example.demo.entity;

import jakarta.validation.constraints.*;

public record OrderRequestDto(@NotNull
                                      String parcelNumber,

                              @NotBlank
                              @Email
                              String emailReceiver,

                              @NotBlank
                              String receiverCountryCode,

                              @NotBlank
                              String senderCountryCode,

                              @Min(0)
                              @Max(100)
                              int statusCode){}
