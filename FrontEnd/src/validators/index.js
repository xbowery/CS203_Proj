import { extend, setInteractionMode } from 'vee-validate'
import { required, email, length, min } from 'vee-validate/dist/rules'

setInteractionMode('eager')

extend('length', length)
extend('min', min)

extend('email', {
  ...email,
  message: 'Invalid Email!',
})

extend('password', {
  validate: value => {
    const strongRegex = /(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9])(?=.{8,})/
    return strongRegex.exec(value) !== null
  },
  message: 'Password is not strong enough!',
})

// Override the default message.
extend('required', {
  ...required,
  message: '{_field_} is required',
})

extend('cfmPassword', {
  params: ['target'],
  validate(value, { target }) {
    return value === target
  },
  message: 'Password does not match!',
})
