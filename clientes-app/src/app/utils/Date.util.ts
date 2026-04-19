export class DateUtil {

  static format(value: string): string {
    if (!value) {
      return '';
    }

    let v = value.replace(/\D/g, '');

    if (v.length > 2) {
      v = v.substring(0, 2) + '/' + v.substring(2);
    }
    if (v.length > 5) {
      v = v.substring(0, 5) + '/' + v.substring(5, 9);
    }

    return v;
  }
  static toBackend(value: string): string {
    if (!value) {
      return '';
    }

    const [dia, mes, ano] = value.split('/');

    return `${dia}/${mes}/${ano}`;
  }

  static fromBackend(value: string): string {
    if (!value) {
      return '';
    }

    const [ano, mes, dia] = value.split('-');

    return `${dia}/${mes}/${ano}`;
  }

}
