<template>
  <v-card>
    <v-card-text class="d-flex align-center flex-wrap pb-0">
      <!-- search -->
      <v-text-field
        v-model="search"
        placeholder="Search"
        outlined
        hide-details
        dense
        class="user-search me-3 mb-4"
        label="Search"
      >
      </v-text-field>
      <v-spacer></v-spacer>
    </v-card-text>
    <v-data-table :headers="headers" :items="items" :search="search" sort-by="username" class="elevation-1">
      <template v-slot:top>
        <v-toolbar flat>
          <v-toolbar-title>Users List</v-toolbar-title>
          <v-divider class="mx-4" inset vertical></v-divider>
          <v-spacer></v-spacer>
          <v-dialog v-model="dialog" max-width="500px">
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                color="primary"
                dark
                class="mb-2"
                @click.stop="isAddNewUserSidebarActive = !isAddNewUserSidebarActive"
                v-bind="attrs"
                v-on="on"
              >
                Add New User
              </v-btn>
            </template>
            <v-card>
              <validation-observer v-slot="{ handleSubmit, invalid }">
                <v-form @submit.prevent="handleSubmit(save)">
                  <v-alert elevation="2" type="error" v-if="message">{{ message }}</v-alert>
                  <v-card-title>
                    <span class="text-h5">{{ formTitle }}</span>
                  </v-card-title>

                  <v-card-text>
                    <v-container>
                      <v-row>
                        <v-col cols="12" sm="6" md="4">
                          <validation-provider name="first name" rules="required" v-slot="{ errors }">
                            <v-text-field
                              v-model="editedItem.firstName"
                              :error-messages="errors[0]"
                              label="First name"
                            ></v-text-field>
                          </validation-provider>
                        </v-col>
                        <v-col cols="12" sm="6" md="4">
                          <validation-provider name="last name" rules="required" v-slot="{ errors }">
                            <v-text-field
                              v-model="editedItem.lastName"
                              :error-messages="errors[0]"
                              label="Last name"
                            ></v-text-field>
                          </validation-provider>
                        </v-col>
                        <v-col cols="12" sm="6" md="4">
                          <validation-provider name="username" rules="required" v-slot="{ errors }">
                            <v-text-field
                              v-model="editedItem.username"
                              :error-messages="errors[0]"
                              label="Username"
                              :disabled="editedIndex > -1"
                            ></v-text-field>
                          </validation-provider>
                        </v-col>
                        <v-col cols="12" sm="6" md="4">
                          <validation-provider name="email" rules="required" v-slot="{ errors }">
                            <v-text-field
                              v-model="editedItem.email"
                              :error-messages="errors[0]"
                              label="Email"
                            ></v-text-field>
                          </validation-provider>
                        </v-col>
                        <v-col cols="12" sm="6" md="4">
                          <validation-provider
                            name="Company"
                            :rules="{ required: editedItem.role === 'EMPLOYEE' || editedItem.role === 'BUSINESS' }"
                            v-slot="{ errors }"
                          >
                            <v-text-field
                              v-model="editedItem.company"
                              :error-messages="errors[0]"
                              label="Company"
                              :disabled="!(editedItem.role === 'EMPLOYEE' || editedItem.role === 'BUSINESS')"
                            ></v-text-field>
                          </validation-provider>
                        </v-col>
                        <v-col cols="12" sm="6" md="4">
                          <validation-provider name="role" rules="required" v-slot="{ errors }">
                            <v-select
                              v-model="editedItem.role"
                              :error-messages="errors[0]"
                              label="Role"
                              :items="roleOptions"
                              item-text="title"
                              item-value="value"
                              outlined
                              dense
                              hide-details="auto"
                              class="mb-6"
                            ></v-select>
                          </validation-provider>
                        </v-col>
                      </v-row>
                    </v-container>
                  </v-card-text>
                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="blue darken-1" text @click="dialog = false" type="button"> Cancel </v-btn>
                    <v-btn color="blue darken-1" text type="submit" :disabled="invalid"> Save </v-btn>
                  </v-card-actions>
                </v-form>
              </validation-observer>
            </v-card>
          </v-dialog>
          <v-dialog v-model="dialogDelete" max-width="500px">
            <v-card>
              <v-card-title class="text-h5">Are you sure you want to delete this user?</v-card-title>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="closeDelete">Cancel</v-btn>
                <v-btn color="blue darken-1" text @click="deleteItemConfirm">OK</v-btn>
                <v-spacer></v-spacer>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-toolbar>
      </template>
      <template #[`item.actions`]="{ item }">
        <v-icon small color="secondary" outlined class="mr-2" @click="editItem(item)"> Edit </v-icon>

        <v-icon small color="secondary" outlined class="mr-2" @click="deleteItem(item)"> Delete </v-icon>
      </template>
      <template v-slot:no-data>
        <p>No data available.</p>
      </template>
    </v-data-table>
  </v-card>
</template>

<script>
import User from '@/model/user'
import UserService from '@/services/user.service'
import { ValidationProvider, ValidationObserver } from 'vee-validate'
import '@/validators'

export default {
  components: { ValidationProvider, ValidationObserver },
  data: () => ({
    dialog: false,
    dialogDelete: false,
    search: '',
    errors: '',
    isAddNewUserSidebarActive: '',
    roleOptions: [
      { title: 'Admin', value: 'ADMIN' },
      { title: 'User', value: 'USER' },
      { title: 'Employee', value: 'EMPLOYEE' },
      { title: 'Business Owner', value: 'BUSINESS' },
    ],
    headers: [
      { text: 'USERNAME', value: 'username' },
      {
        text: 'FIRST NAME',
        align: 'start',
        sortable: false,
        value: 'firstName',
      },
      { text: 'LAST NAME', value: 'lastName' },
      { text: 'EMAIL', value: 'email' },
      { text: 'COMPANY', value: 'company' },
      { text: 'ROLE', value: 'role' },
      { text: 'ACTIONS', value: 'actions', sortable: true },
    ],
    items: [],
    editedIndex: -1,
    editedItem: {
      firstName: '',
      lastName: '',
      username: '',
      email: '',
      company: '',
      authorities: '',
    },
    defaultItem: {
      firstName: '',
      lastName: '',
      username: '',
      email: '',
      company: '',
      authorities: '',
    },
  }),

  async mounted() {
    try {
      const res = await UserService.getUsers()
      this.items = res.data
    } catch (error) {
      console.error(error)
    }
  },

  computed: {
    formTitle() {
      return this.editedIndex === -1 ? 'New User' : 'Edit User'
    },
  },

  watch: {
    dialog(val) {
      val || this.close()
    },
    dialogDelete(val) {
      val || this.closeDelete()
    },
  },

  setup() {
    const user = new User('', '', '', '')
    const username = null
    const message = null
    return {
      user,
      username,
      message,
    }
  },

  methods: {
    editItem(item) {
      this.editedIndex = this.items.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialog = true
    },

    deleteItem(item) {
      this.editedIndex = this.items.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialogDelete = true
    },

    deleteItemConfirm() {
      const user = new User('')
      user.username = this.items[this.editedIndex].username

      this.items.splice(this.editedIndex, 1)
      this.handleDeleteUser(user)

      if (!this.message) {
        this.closeDelete()
      }
    },

    close() {
      this.dialog = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },

    closeDelete() {
      this.dialogDelete = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },

    save() {
      if (this.editedIndex > -1) {
        Object.assign(this.items[this.editedIndex], this.editedItem)

        const user = new User('', '', '', '')
        user.username = this.items[this.editedIndex].username
        user.firstName = this.items[this.editedIndex].firstName
        user.lastName = this.items[this.editedIndex].lastName
        user.email = this.items[this.editedIndex].email
        user.authorities = this.items[this.editedIndex].authorities
        this.handleEditUser(user)
      } else {
        this.items.push(this.editedItem)
        // this.handleSaveUser(this.editedItem)
      }
      if (!this.message) {
        this.close()
      }
    },

    async handleEditUser(user) {
      try {
        const res = await UserService.updateUser(user)
        console.log(res.data)
      } catch (error) {
        this.message = error.response?.data?.message || error.message || error.toString()
        console.error(error)
      }
    },
    // async handleNewUser(user) {
    //   try {
    //     const res = UserService.saveUser(user)
    //     console.log(res.data)
    //   } catch (error) {
    //     this.message = error.response?.data?.message || error.message || error.toString()
    //     console.log(error)
    //   }
    // },

    async handleDeleteUser(user) {
      try {
        const res = await UserService.deleteUser(user)
        console.log(res.data)
      } catch (error) {
        this.message = error.response?.data?.message || error.message || error.toString()
        console.error(error)
      }
    },
  },
}
</script>
