import Q from "../utils/core";

function isJson(constraint, val) {
  let message = constraint.message;
  try {
    if (Q.isEmpty(val) || typeof JSON.parse(val) === "object") {
      return Q.newSuccessValidate('');
    }
  } catch (e) {

  }
  return Q.newErrorValidate(message || "输入需要为JSON格式");
}

export default (constraint, val) => isJson(constraint, val);
