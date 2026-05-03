export class DateUtil {

  static format(value: string): string {
    if (!value) {
      return '';
    }

    let formattedValue = value.replace(/\D/g, '');
    formattedValue = formattedValue.substring(0, 8);

    if (formattedValue.length > 2) {
      formattedValue = `${formattedValue.substring(0, 2)}/${formattedValue.substring(2)}`;
    }

    if (formattedValue.length > 5) {
      formattedValue = `${formattedValue.substring(0, 5)}/${formattedValue.substring(5, 9)}`;
    }

    return formattedValue;
  }

  static isValidDateBr(value: string): boolean {
    if (!value) {
      return false;
    }

    const regex = /^(\d{2})\/(\d{2})\/(\d{4})$/;
    const match = value.match(regex);

    if (!match) {
      return false;
    }

    const day = Number(match[1]);
    const month = Number(match[2]);
    const year = Number(match[3]);

    if (month < 1 || month > 12) {
      return false;
    }

    if (day < 1 || day > 31) {
      return false;
    }

    const date = new Date(year, month - 1, day);

    return date.getFullYear() === year &&
      date.getMonth() === month - 1 &&
      date.getDate() === day;
  }

  static isBeforeMinDate(value: string, minYear = 2000): boolean {
    if (!this.isValidDateBr(value)) {
      return true;
    }

    const [, , year] = value.split('/').map(Number);

    return year < minYear;
  }

  static isFutureBeyondYears(value: string, maxYearsAhead = 1): boolean {
    if (!this.isValidDateBr(value)) {
      return true;
    }

    const [day, month, year] = value.split('/').map(Number);
    const informedDate = new Date(year, month - 1, day);

    const today = new Date();
    const maxDate = new Date();
    maxDate.setFullYear(today.getFullYear() + maxYearsAhead);

    return informedDate > maxDate;
  }

  static isPastDate(value: string): boolean {
    if (!this.isValidDateBr(value)) {
      return true;
    }

    const [day, month, year] = value.split('/').map(Number);
    const informedDate = new Date(year, month - 1, day);

    const today = new Date();
    today.setHours(0, 0, 0, 0);
    informedDate.setHours(0, 0, 0, 0);

    return informedDate < today;
  }

  static toBackend(value: string): string {
    return value || '';
  }

  static fromBackend(value: string): string {
    if (!value) {
      return '';
    }

    if (value.includes('/')) {
      return value;
    }

    const [year, month, day] = value.split('-');

    if (!year || !month || !day) {
      return value;
    }

    return `${day}/${month}/${year}`;
  }
}
