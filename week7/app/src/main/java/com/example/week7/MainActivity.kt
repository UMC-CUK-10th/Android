package com.example.week7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*

// 1. 화면 라우팅 정의
sealed class Screen(val route: String, val iconRes: Int, val label: String) {
    object Home : Screen("home", R.drawable.home, "홈")
    object Shop : Screen("shop", R.drawable.buyimage, "구매하기")
    object Wishlist : Screen("wishlist", R.drawable.wishimage, "위시리스트")
    object Bag : Screen("bag", R.drawable.bagimage, "장바구니")
    object Profile : Screen("profile", R.drawable.profileimage, "프로필")
}

// 2. 데이터 모델
data class Product(
    val id: String,
    val name: String,
    val subText: String,
    val price: String,
    val imageRes: Int
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { NikeBottomNavigation(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Shop.route) { ShopScreen() }
            composable(Screen.Wishlist.route) { WishlistScreen() }
            composable(Screen.Bag.route) {
                BagScreen(onOrderClick = {
                    navController.navigate(Screen.Shop.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            }
            composable(Screen.Profile.route) { ProfileScreen() }
        }
    }
}

@Composable
fun NikeBottomNavigation(navController: NavHostController) {
    val items = listOf(Screen.Home, Screen.Shop, Screen.Wishlist, Screen.Bag, Screen.Profile)

    NavigationBar(containerColor = Color.White) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = screen.iconRes),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(screen.label, fontSize = 10.sp) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color.LightGray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

// ==========================================
// 개별 화면 구현부
// ==========================================

@Composable
fun HomeScreen() {
    val scrollState = rememberScrollState()
    val homeProducts = listOf(
        Product("home_1", "Air Jordan XXXVI", "Men's Shoes", "US$185", R.drawable.nikeaf1),
        Product("home_2", "Nike Air Force 1 '07", "Men's Shoes", "US$165", R.drawable.nikeaf2)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // 1. 타이틀 영역
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "Discover",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "9월 4일 목요일",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. 메인 배너
        Image(
            painter = painterResource(id = R.drawable.homebanner),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(horizontal = 24.dp), // 양옆에 여백 주기 적용
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 3. 섹션 타이틀
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "What's new",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "나이키 최신 상품",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4. 가로 스크롤 상품 리스트
        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = homeProducts, key = { it.id }) { product ->
                Column(modifier = Modifier.width(260.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .background(Color(0xFFF6F6F6))
                    ) {
                        Image(
                            painter = painterResource(id = product.imageRes),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = product.name, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(text = product.price, fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    } // <- 닫는 괄호 에러 나던 부분 해결!
}

@Composable
fun ShopScreen() {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("전체", "Tops & T-Shirts", "sale")

    val shopProducts = listOf(
        Product("shop_1", "Nike Everyday Plus Cushioned", "Training Ankle Socks (6 Pairs)", "US$10", R.drawable.nikesocks),
        Product("shop_2", "Nike Elite Crew", "Basketball Socks\n7 Colours", "US$16", R.drawable.nikeaf3),
        Product("shop_3", "Nike Air Force 1 '07", "Women's Shoes\n5 Colours", "US$115", R.drawable.nikeaf4),
        Product("shop_4", "Jordan Nike Air Force 1 '07Essentials", "Men's Shoes\n2 Colours", "US$115", R.drawable.nikeshoes)
    )

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Spacer(modifier = Modifier.height(40.dp))

        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.White,
            contentColor = Color.Black,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    height = 2.dp,
                    color = Color.Black
                )
            },
            divider = { HorizontalDivider(color = Color(0xFFEEEEEE)) }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 14.sp,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTabIndex == index) Color.Black else Color.Gray
                        )
                    }
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items = shopProducts, key = { it.id }) { product ->
                ShopProductItem(product)
            }
        }
    }
}

@Composable
fun ShopProductItem(product: Product) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Color(0xFFF6F6F6))
        ) {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Icon(
                painter = painterResource(id = R.drawable.wishimage),
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = product.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(text = product.subText, fontSize = 11.sp, color = Color.Gray, minLines = 2, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Text(text = product.price, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
fun WishlistScreen() {
    val wishlistProducts = listOf(
        Product("wish_1", "Air Jordan 1 Mid", "Shoes", "US$125", R.drawable.nikesocks),
        Product("wish_2", "Nike Everyday Plus Cushioned", "Training Ankle Socks (6 Pairs)\n5 Colours", "US$10", R.drawable.jordan1)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "위시리스트",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items = wishlistProducts, key = { it.id }) { product ->
                ShopProductItem(product)
            }
        }
    }
}

@Composable
fun BagScreen(onOrderClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .border(1.5.dp, Color.Black, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.bagimage),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "장바구니가 비어 있습니다.\n제품을 추가하면 여기에 표시됩니다.",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Button(
            onClick = onOrderClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 32.dp)
                .height(60.dp)
                .align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(30.dp)
        ) {
            Text(
                text = "주문하기",
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun ProfileScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "프로필 화면")
    }
}