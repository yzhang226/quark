SELECT
  TABLE_SCHEMA AS TABLE_CATALOG,
  NULL AS TABLE_SCHEMA,
  TABLE_NAME,
  COLUMN_NAME,
  CHARACTER_SET_NAME,
  COLLATION_NAME,
  COLUMN_TYPE,
  EXTRA
FROM 
  INFORMATION_SCHEMA.COLUMNS
