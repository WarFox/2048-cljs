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
        },
        brown: {
          50: '#fdf8f6',
          100: '#f2e8e5',
          200: '#eaddd7',
          300: '#e0cec7',
          400: '#d2bab0',
          500: '#bfa094',
          600: '#a18072',
          700: '#977669',
          800: '#846358',
          900: '#43302b',
        },
      }
    }
  },
  variants: {},
  plugins: [],
}
