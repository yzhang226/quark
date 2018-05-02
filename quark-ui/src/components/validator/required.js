
function exist(obj) {
  return obj !== undefined && obj != null && obj !== '';
}

export default obj => exist(obj);
