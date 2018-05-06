
import ValidateResult from '../validator/ValidateResult'

let Q = {};

Q.isEmpty = function(obj) {
  return obj === undefined || obj == null || obj === '';
};

Q.isNotEmpty = function (obj) {
  return !Q.isEmpty(obj)
};

Q.isArrayEmpty = function (arr) {
  return Q.isEmpty(arr) || arr.length === 0;
};

Q.isArrayNotEmpty = function (arr) {
  return !Q.isArrayEmpty(arr);
};

Q.newErrorValidate = function (msg) {
  return new ValidateResult(-1, msg);
};

Q.newSuccessValidate = function (msg) {
  return new ValidateResult(0, msg);
};

Q.filterOneErrorValidate = function (res) {
  if (Q.isEmpty(res)) {
    return null;
  }
  if (res instanceof Array) {
    for (let i=0; i<res.length; i++) {
      let re = res[i];
      if (Q.isNotEmpty(re) && re.isError()) {
        return re;
      }
    }
  } else if (res instanceof ValidateResult) {
    if (res.isError()) {
      return res;
    }
  }

  return null;
};

Q.resolveValidateConstraint = function (validations, modelExpression) {
  if (Q.isEmpty(validations)) {
    return null;
  }
  let idx = modelExpression.indexOf(".");
  if (idx > -1) {
    let name1 = modelExpression.substring(0, idx);
    let name2 = modelExpression.substring(idx+1);
    return Q.resolveValidateConstraint(validations[name1], name2);
  } else {
    return validations[modelExpression];
  }
};

export default Q;

