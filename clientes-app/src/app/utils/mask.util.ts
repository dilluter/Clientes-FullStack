export class MaskUtil {

  static cpf(value: string): string {
    if (!value) {
      return '';
    }

    let cpf = value.replace(/\D/g, '');
    cpf = cpf.substring(0, 11);

    if (cpf.length > 9) {
      return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{1,2})/, '$1.$2.$3-$4');
    }

    if (cpf.length > 6) {
      return cpf.replace(/(\d{3})(\d{3})(\d{1,3})/, '$1.$2.$3');
    }

    if (cpf.length > 3) {
      return cpf.replace(/(\d{3})(\d{1,3})/, '$1.$2');
    }

    return cpf;
  }

  static phone(value: string): string {
    if (!value) {
      return '';
    }

    let phone = value.replace(/\D/g, '');
    phone = phone.substring(0, 11);

    if (phone.length > 10) {
      return phone.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3');
    }

    if (phone.length > 6) {
      return phone.replace(/(\d{2})(\d{4})(\d{1,4})/, '($1) $2-$3');
    }

    if (phone.length > 2) {
      return phone.replace(/(\d{2})(\d{1,5})/, '($1) $2');
    }

    return phone;
  }

  static onlyNumbers(value: string): string {
    if (!value) {
      return '';
    }

    return value.replace(/\D/g, '');
  }
}
