## Pokemon 
  - This app makes use of `https://pokeapi.co/` API provider to display a list of pokemons and details about them.
  - The pattern is straightforward and easy to follow so that any android dev should be able to make his/her own features and test.

### UI
  - The UI makes use of ConstraintLayout but this should be easily convertible to a Jetpack compose because it makes use of Kotlin Flow and ViewStates to update its UI

### Architecture Pattern
  - The architecture used is MVI with Repository Pattern
  
### Navigation
  - Jetpack Navigation
  
### Tech stack
  - Kotlin Flow / Coroutines
  - Retrofit2
  - Dagger Hilt
  - View Binding
  - ViewModel
  - Glide

### Unit Test
  - ViewModel Unit Test
  - Mappers Unit Test
