package com.example.clinica_medica.utils;

public class CPFUtils {

  public static boolean isCPFValido(String cpf) {
    if (cpf == null || cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
      return false;
    }

    try {
      int[] pesos1 = {10, 9, 8, 7, 6, 5, 4, 3, 2};
      int[] pesos2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

      int soma1 = 0, soma2 = 0;
      for (int i = 0; i < 9; i++) {
        int digito = Character.getNumericValue(cpf.charAt(i));
        soma1 += digito * pesos1[i];
        soma2 += digito * pesos2[i];
      }

      int digito1 = (soma1 % 11 < 2) ? 0 : 11 - (soma1 % 11);
      soma2 += digito1 * pesos2[9];
      int digito2 = (soma2 % 11 < 2) ? 0 : 11 - (soma2 % 11);

      return digito1 == Character.getNumericValue(cpf.charAt(9))
          && digito2 == Character.getNumericValue(cpf.charAt(10));
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
