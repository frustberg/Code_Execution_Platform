/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./index.html', './src/**/*.{ts,tsx}'],
  theme: {
    extend: {
      colors: {
        bg: '#050816',
        surface: '#0b1020',
        surfaceAlt: '#111827',
        accent: '#6366f1',
        accentSoft: '#4f46e5',
        success: '#22c55e',
        danger: '#ef4444',
        muted: '#6b7280'
      },
      fontFamily: {
        sans: ['system-ui', 'ui-sans-serif', 'Inter', 'sans-serif']
      }
    }
  },
  plugins: []
}