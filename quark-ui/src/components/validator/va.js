
import required from './required'


let container = {
  'required': required
};

// function validate(type, regex) {
//   let typeValidator = container[type];
//   if (typeValidator !== undefined && typeValidator != null) {
//     typeValidator()
//   }
// }

export default (type, regex) => validate(type, regex) ;
