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
  <v-data-table
    :headers="headers"
    :items="items"
    :search = "search"
    sort-by="username"
    class="elevation-1"
  >
    <template v-slot:top>
      <v-toolbar
        flat
      >
        <v-toolbar-title>Users List</v-toolbar-title>
        <v-divider
          class="mx-4"
          inset
          vertical
        ></v-divider>
        <v-spacer></v-spacer>
        <v-dialog
          v-model="dialog"
          max-width="500px"
        >
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
          <validation-observer v-slot="{ handleSubmit, invalid }">
          <!-- <v-card slot-scope="{ handleSubmit, invalid }"> -->
            <!-- <v-alert elevation="2" type="error" v-if="this.message">{{ this.message }}</v-alert> -->
            <v-form @submit.prevent="handleSubmit(handleUser)">
            <v-card-title>
              <span class="text-h5">{{ formTitle }}</span>
            </v-card-title>

            <v-card-text>
              <v-container>
                <v-row>
                  <v-col
                    cols="12"
                    sm="6"
                    md="4"
                  >
                  <validation-provider name="first name" rules="required" v-slot="{ errors }">
                    <v-text-field
                      v-model="editedItem.firstName"
                      :error-messages="errors[0]"
                      label="First name"
                    ></v-text-field>
                  </validation-provider>
                  </v-col>
                  <v-col
                    cols="12"
                    sm="6"
                    md="4"
                  >
                  <validation-provider name="last name" rules="required" v-slot="{ errors }">
                    <v-text-field
                      v-model="editedItem.lastName"
                      :error-messages="errors[0]"
                      label="Last name"
                    ></v-text-field>
                  </validation-provider>
                  </v-col>
                  <v-col
                    cols="12"
                    sm="6"
                    md="4"
                  >
                  <validation-provider name="username" rules="required" v-slot="{ errors }">
                    <v-text-field
                      v-model="editedItem.username"
                      :error-messages="errors[0]"
                      label="Username"
                    ></v-text-field>
                  </validation-provider>
                  </v-col>
                  <v-col
                    cols="12"
                    sm="6"
                    md="4"
                  >
                  <validation-provider name="email" rules="required" v-slot="{ errors }">
                    <v-text-field
                      v-model="editedItem.email"
                      :error-messages="errors[0]"
                      label="Email"
                    ></v-text-field>
                  </validation-provider>
                  </v-col>
                  <v-col
                    cols="12"
                    sm="6"
                    md="4"
                  >
                  <validation-provider name="company" rules="required" v-slot="{ errors }">
                    <v-text-field
                      v-model="editedItem.company"
                      :error-messages="errors[0]"
                      label="Company"
                    ></v-text-field>
                  </validation-provider>
                  </v-col>
                  <v-col
                    cols="12"
                    sm="6"
                    md="4"
                  >
                  <validation-provider name="role" rules="required" v-slot="{ errors }">
                    <v-select
                      v-model="editedItem.authorities"
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
              <v-btn
                color="blue darken-1"
                text
                @click="dialog = false" 
                type="button"
              >
                Cancel
              </v-btn>
              <v-btn
                color="blue darken-1"
                text
                @click="save"
                type="submit" :disabled="invalid"
              >
                Save
              </v-btn>
            </v-card-actions>
            </v-form>
          <!-- </v-card> -->
          </validation-observer>
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
    <template  #[`item.actions`]="{ item }">
        <v-icon
              small
              color="secondary"
              outlined
              class="mr-2"

              @click="editItem(item)"
            >
              Edit
        </v-icon>

        <v-icon
              small
              color="secondary"
              outlined
              class="mr-2"

              @click="deleteItem(item)"
            >
              Delete
        </v-icon>
    </template>
    <template v-slot:no-data>
      <v-text>No data available.</v-text>
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
      deleteConfirm: false,
      newUserConfirm: false,
      editUserConfirm: true,
      search:'',
      errors:'',
      isAddNewUserSidebarActive: '',
      roleOptions: [
      { title: 'Admin', value: 'admin' },
      { title: 'User', value: 'user' },
      { title: 'Employee', value: 'employee' },
      { title: 'Business Owner', value: 'business owner' },
    ],
      headers: [
        {
          text: 'FIRST NAME',
          align: 'start',
          sortable: false,
          value: 'firstName',
        },
        { text: 'LAST NAME', value: 'lastName' },
        { text: 'USERNAME', value: 'username' },
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
      // user: new User('', '', '', ''),
      // username: null,
      // message: null,
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
      formTitle () {
        return this.editedIndex === -1 ? 'New User' : 'Edit User'
      },
    },

    watch: {
      dialog (val) {
        val || this.close()
      },
      dialogDelete (val) {
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
      editItem (item) {
        this.editedIndex = this.items.indexOf(item)
        this.editedItem = Object.assign({}, item)
        this.dialog = true
      },

      deleteItem (item) {
        this.editedIndex = this.items.indexOf(item)
        this.editedItem = Object.assign({}, item)
        this.dialogDelete = true
      },

      deleteItemConfirm () {
        this.items.splice(this.editedIndex, 1)
        this.deleteConfirm = true
        this.closeDelete()
      },

      close () {
        this.dialog = false
        this.$nextTick(() => {
          this.editedItem = Object.assign({}, this.defaultItem)
          this.editedIndex = -1
        })
      },

      closeDelete () {
        this.dialogDelete = false
        this.$nextTick(() => {
          this.editedItem = Object.assign({}, this.defaultItem)
          this.editedIndex = -1
        })
      },

      save() {
        if (this.editedIndex > -1) {
          Object.assign(this.items[this.editedIndex], this.editedItem)
          this.username = this.items[this.editedIndex].username
          this.user.firstName = this.items[this.editedIndex].firstName
          this.user.lastName = this.items[this.editedIndex].lastName
          this.user.email = this.items[this.editedIndex].email
          this.user.authorities = this.items[this.editedIndex].authorities
          console.log(this.user.authorities)
          this.editUserConfirm = true
          console.log(this.editUserConfirm)
        } else {
          this.items.push(this.editedItem)
          this.newUserConfirm = true
        }
        this.close()
      },
      
      async handleUser() {
        if (this.editUserConfirm) {
          try {
            const res = this.$store.dispatch('auth/updateUser', this.username, this.user)
            console.log(res)
          } catch (error) {
            this.message = error.response?.data?.message || error.message || error.toString()
            console.log(error)
          }
        }
      }
    },
  }

</script>