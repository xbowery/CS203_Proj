// eslint-disable-next-line object-curly-newline
import { getCurrentInstance, reactive, toRefs, watch } from '@vue/composition-api'

// Thanks: https://medium.com/better-programming/reactive-vue-routes-with-the-composition-api-18c1abd878d1
export const useRouter = () => {
  const vm = getCurrentInstance().proxy

  const state = reactive({
    route: vm.$route,
  })

  watch(
    () => vm.$route,
    r => {
      state.route = r
    },
  )

  return { ...toRefs(state), router: vm.$router }
}

export const isEmpty = value => {
  if (value === null || value === undefined || value === '') {
    return true
  }

  if (Array.isArray(value) && value.length === 0) {
    return true
  }

  return false
}

// ——— Get Initial Text from name ——————— //

export const getInitialName = fullName =>
  // eslint-disable-next-line implicit-arrow-linebreak
  fullName
    .split(' ')
    .map(n => n[0])
    .join('')

// ——— Add Alpha To color ——————— //

export const addAlpha = (color, opacity) => {
  const opacityLocal = Math.round(Math.min(Math.max(opacity || 1, 0), 1) * 255)

  return color + opacityLocal.toString(16).toUpperCase()
}

// ——— Perfect Scrollbar Scroll to bottom ——————— //

export const psScrollToBottom = psRef => () => {
  const scrollEl = psRef.value.$el || psRef.value
  scrollEl.scrollTop = scrollEl.scrollHeight
}

// ——— Perfect Scrollbar Scroll to bottom ——————— //

export const psScrollToTop = psRef => () => {
  const scrollEl = psRef.value.$el || psRef.value
  scrollEl.scrollTop = 0
}
