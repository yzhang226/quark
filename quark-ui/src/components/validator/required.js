import Q from "../utils/core";

function exist(constraint, val) {
  let res = Q.isNotEmpty(val);
  let message = constraint.message;
  if (res === false) {
    return Q.newErrorValidate(message || "输入不能为空");
  }
  return Q.newSuccessValidate('');

}

export default (constraint, val) => exist(constraint, val);
