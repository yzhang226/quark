
class ValidateResult {
  constructor(status, message) {
    this.status = status;
    this.message = message;
  }
  isSuccess() {
    return this.status === 0;
  }
  isError() {
    return this.status !== 0;
  }
}

export default ValidateResult;

