package com.itau.challenge.model;
import lombok.Getter;


@Getter
public enum ErrorCode {
    ERR001("JWT inválido - estrutura do token está incorreta ou não pode ser parseada"),
    ERR002("Quantidade de claims incorreta - o JWT deve conter exatamente 3 claims"),
    ERR003("Claim Name contém caracteres numéricos"),
    ERR004("Claim Name excede o tamanho máximo de 256 caracteres"),
    ERR005("Claim Role inválida - deve ser um dos valores: Admin, Member ou External"),
    ERR006("Claim Seed não é um número primo"),
    ERR007("Claim Seed não é um número válido"),
    ERR008("Claim obrigatória ausente"),
    ERR009("Erro inesperado durante a validação");

    private final String description;

    ErrorCode(String description) {
        this.description = description;
    }
}
