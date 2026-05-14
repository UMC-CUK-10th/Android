package com.example.week07

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.week07.R

sealed class BottomNavItem(val route: String, val title: String, val icon: Int) {
    object Home: BottomNavItem("home","홈",R.drawable.ic_housesimple)
    object Purchase: BottomNavItem("purchase","구매하기", R.drawable.ic_listmagnifyingglass)
    object Wishlist: BottomNavItem("wishlist","위시리스트", R.drawable.ic_heartstraight)
    object Cart: BottomNavItem("cart","장바구니", R.drawable.ic_bagsimple)
    object Profile: BottomNavItem("profile","장바구니", R.drawable.ic_user)
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Purchase,
        BottomNavItem.Wishlist,
        BottomNavItem.Cart,
        BottomNavItem.Profile
    )

    NavigationBar {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route){
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop=true
                    }
                },
                icon = {
                    Icon (
                        painter = painterResource(id=item.icon),
                        contentDescription = item.title
                    )
                },
                label = { Text(text=item.title) }
            )
        }
    }
}