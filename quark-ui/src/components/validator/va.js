
import required from './required'
import length from './length'
import json from './json'


let container = {
  'required': required,
  'length': length,
  'json': json
};

function validateClosure(constraints) {
  return function (...args) {
    try {
      return validate(constraints);
    } finally {
    }
  }
}

let validator = {};

validator.validate = function (constraintOptions, val) {
  let keys = Object.getOwnPropertyNames(constraintOptions);
  let res = [];
  for (let i=0; i<keys.length; i++) {
    let key = keys[i];
    let constraint = constraintOptions[key];
    let typeValidator = container[key];
    if (Q.isNotEmpty(typeValidator)) {
      let r = typeValidator(constraint, val);
      res.push(r);
    }
  }
  return res;
};

export default validator;
