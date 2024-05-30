module.exports = {
  mode: 'jit',
  content: [
    './resources/**/*.html',
    './resources/**/*.js'
  ],
  theme: {
    extend: {
      colors: {
        yellow: {
          250: '#FEE869'
        },
        amber: {
          250: '#FDDD6C'
        }
      }
    }
  },
  variants: {},
  plugins: [],
}
