import { HttpErrorResponse } from '@angular/common/http';
import { ApiErrorResponse } from '../shared/api-error';

export class ErrorUtil {

  static getErrors(errorResponse: HttpErrorResponse, defaultMessage: string): string[] {
    const errorBody = errorResponse.error as ApiErrorResponse | string | string[] | null;

    if (Array.isArray(errorBody)) {
      return errorBody;
    }

    if (typeof errorBody === 'string' && errorBody.trim().length > 0) {
      return [errorBody];
    }

    if (errorBody && typeof errorBody === 'object') {
      if (Array.isArray(errorBody.errors) && errorBody.errors.length > 0) {
        return errorBody.errors;
      }

      if (errorBody.error) {
        return [errorBody.error];
      }

      if (errorBody.message) {
        return [errorBody.message];
      }
    }

    return [defaultMessage];
  }
}
