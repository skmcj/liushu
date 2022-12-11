module.exports = {
  root: true,
  env: {
    node: true
  },
  extends: [
    'plugin:vue/essential',
    '@vue/standard'
  ],
  parserOptions: {
    parser: '@babel/eslint-parser'
  },
  rules: {
    'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'space-before-function-paren': ['error', 'never'],
    'semi': ['off', 'always'],
    'vue/multi-word-component-names': 'off',
    'keyword-spacing': 'off',
    'no-unused-vars': 'off',
    'prefer-const': 'off',
    'quote-props': 'off',
    'vue/no-unused-components': 'off',
    'no-multiple-empty-lines': 'off',
    'no-multi-spaces': 'off',
    'node/handle-callback-err': 'off',
    'operator-linebreak': 'off',
    'array-bracket-spacing': 'off',
    'padded-blocks': 'off',
    'no-unneeded-ternary': 'off'
  }
}
