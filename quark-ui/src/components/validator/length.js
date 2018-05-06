
import Q from '../utils/core'

function check(constraint, val) {
  let targetLength = Q.isNotEmpty(val) ? val.length : 0;
  let min = constraint.min;
  let max = constraint.max;
  let message = constraint.message;
  if (Q.isNotEmpty(min) && targetLength < parseInt(min)) {
    return Q.newErrorValidate(message || "长度最小为" + min);
  }
  if (Q.isNotEmpty(max) && targetLength > parseInt(max)) {
    return Q.newErrorValidate(message || "长度最大为" + max);
  }
  return Q.newSuccessValidate('');
}

export default (constraint, val) => check(constraint, val);
