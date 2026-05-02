export class DateUtil {

  static format(value: string): string {
    if (!value) {
      return '';
    }

    let formattedValue = value.replace(/\D/g, '');

    if (formattedValue.length > 2) {
      formattedValue = `${formattedValue.substring(0, 2)}/${formattedValue.substring(2)}`;
    }

    if (formattedValue.length > 5) {
      formattedValue = `${formattedValue.substring(0, 5)}/${formattedValue.substring(5, 9)}`;
    }

    return formattedValue;
  }

  static toBackend(value: string): string {
    if (!value) {
      return '';
    }

    const [dia, mes, ano] = value.split('/');

    if (!dia || !mes || !ano) {
      return value;
    }

    return `${dia}/${mes}/${ano}`;
  }

  static fromBackend(value: string): string {
    if (!value) {
      return '';
    }

    if (value.includes('/')) {
      return value;
    }

    const [ano, mes, dia] = value.split('-');

    if (!ano || !mes || !dia) {
      return value;
    }

    return `${dia}/${mes}/${ano}`;
  }

}
