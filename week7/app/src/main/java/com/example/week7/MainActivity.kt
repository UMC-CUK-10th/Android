package com.example.week7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import coil.compose.AsyncImage
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import androidx.compose.foundation.pager.PageSize

// ==========================================
// 1. 네비게이션 라우팅 정의 및 데이터 모델 선언
// ==========================================

sealed class Screen(val route: String, val iconRes: Int, val label: String) {
    object Home : Screen("home", R.drawable.home, "홈")
    object Shop : Screen("shop", R.drawable.buyimage, "구매하기")
    object Wishlist : Screen("wishlist", R.drawable.wishimage, "위시리스트")
    object Bag : Screen("bag", R.drawable.bagimage, "장바구니")
    object Profile : Screen("profile", R.drawable.profileimage, "프로필")
}

data class Product(
    val id: String,
    val name: String,
    val subText: String,
    val price: String,
    val imageRes: Int
)

// ReqRes API 응답용 데이터 구조체
data class ReqResResponse(
    val data: UserData
)

data class UserData(
    val id: Int,
    val email: String,
    val first_name: String,
    val last_name: String,
    val avatar: String
)

// Retrofit 서비스 인터페이스
interface ReqResService {
    @GET("api/users/{id}")
    suspend fun getUser(@Path("id") id: Int): ReqResResponse
}

// Retrofit API 클라이언트 싱글톤 오브젝트
object ApiClient {
    val service: ReqResService by lazy {
        Retrofit.Builder()
            .baseUrl("https://reqres.in/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReqResService::class.java)
    }
}

// ==========================================
// 2. 메인 액티비티 및 기본 구조 영역
// ==========================================

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

    // 💡 [실시간 위시리스트 연동 변수] 하트가 눌린 상품의 ID들을 추적 관리하는 리스트 상태
    val wishlistedIds = remember { mutableStateListOf<String>() }

    Scaffold(
        bottomBar = { NikeBottomNavigation(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen() }

            // 구매하기 화면에 하트 상태 전송
            composable(Screen.Shop.route) {
                ShopScreen(
                    wishlistedIds = wishlistedIds,
                    onWishClick = { productId ->
                        if (wishlistedIds.contains(productId)) {
                            wishlistedIds.remove(productId)
                        } else {
                            wishlistedIds.add(productId)
                        }
                    }
                )
            }

            // 위시리스트 화면에 하트 리스트 전송
            composable(Screen.Wishlist.route) {
                WishlistScreen(
                    wishlistedIds = wishlistedIds,
                    onWishClick = { productId ->
                        wishlistedIds.remove(productId) // 위시리스트에서 하트 풀면 즉시 해제
                    }
                )
            }

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
// 3. 개별 화면 구현부 (Jetpack Compose)
// ==========================================

@Composable
fun HomeScreen() {
    val scrollState = rememberScrollState()
    val homeProducts = listOf(
        Product("home_1", "Air Jordan XXXVI", "Men's Shoes", "US$185", R.drawable.nikeaf1),
        Product("home_2", "Air Jordan XXXVI Low", "Men's Shoes", "US$165", R.drawable.nikeaf2)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(48.dp))

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

        Image(
            painter = painterResource(id = R.drawable.homebanner),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(horizontal = 24.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(32.dp))

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
    }
}

@Composable
fun ShopScreen(
    wishlistedIds: List<String>,
    onWishClick: (String) -> Unit
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("전체", "Tops & T-Shirts", "sale")

    val shopProducts = listOf(
        Product("shop_1", "Nike Everyday Plus Cushioned", "Training Ankle Socks (6 Pairs)", "US$10", R.drawable.nikeshoes),
        Product("shop_2", "Nike Elite Crew", "Basketball Socks\n7 Colours", "US$16", R.drawable.nikesocks),
        Product("shop_3", "Nike Air Force 1 '07", "Women's Shoes\n5 Colours", "US$115", R.drawable.nikeaf3),
        Product("shop_4", "Jordan Nike Air Force 1 '07Essentials", "Men's Shoes\n2 Colours", "US$115", R.drawable.nikeaf4)
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
                ShopProductItem(
                    product = product,
                    isWishlisted = wishlistedIds.contains(product.id),
                    onWishClick = onWishClick
                )
            }
        }
    }
}

@Composable
fun ShopProductItem(
    product: Product,
    isWishlisted: Boolean,
    showFavoriteButton: Boolean = true, // 💡 기본값은 true로 두어 구매하기 화면에선 보이게 합니다.
    onWishClick: (String) -> Unit = {}
) {
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

            // 💡 showFavoriteButton이 true일 때만 하트 아이콘을 그립니다.
            if (showFavoriteButton) {
                IconButton(
                    onClick = { onWishClick(product.id) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.wishimage),
                        contentDescription = null,
                        tint = if (isWishlisted) Color.Red else Color.LightGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = product.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(text = product.subText, fontSize = 11.sp, color = Color.Gray, minLines = 2, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Text(text = product.price, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
fun WishlistScreen(
    wishlistedIds: List<String>,
    onWishClick: (String) -> Unit
) {
    val allShopProducts = listOf(
        Product("shop_1", "Nike Everyday Plus Cushioned", "Training Ankle Socks (6 Pairs)", "US$10", R.drawable.nikeshoes),
        Product("shop_2", "Nike Elite Crew", "Basketball Socks\n7 Colours", "US$16", R.drawable.nikesocks),
        Product("shop_3", "Nike Air Force 1 '07", "Women's Shoes\n5 Colours", "US$115", R.drawable.nikeaf3),
        Product("shop_4", "Jordan Nike Air Force 1 '07Essentials", "Men's Shoes\n2 Colours", "US$115", R.drawable.nikeaf4)
    )

    val defaultWishlist = listOf(
        Product("wish_1", "Air Jordan 1 Mid", "Shoes", "US$125", R.drawable.jordan1),
        Product("wish_2", "Nike Everyday Plus Cushioned", "Training Ankle Socks (6 Pairs)\n5 Colours", "US$10", R.drawable.nikesocks)
    )

    val combinedWishlist = remember(wishlistedIds) {
        val added = allShopProducts.filter { wishlistedIds.contains(it.id) }
        defaultWishlist + added
    }

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
            items(items = combinedWishlist, key = { it.id }) { product ->
                // 💡 여기에 showFavoriteButton = false를 주어 하트를 숨깁니다!
                ShopProductItem(
                    product = product,
                    isWishlisted = true,
                    showFavoriteButton = false
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileScreen() {
    var userData by remember { mutableStateOf<UserData?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val response = ApiClient.service.getUser(1)
            userData = response.data
        } catch (e: Exception) {
            e.printStackTrace()
            userData = UserData(
                id = 1,
                email = "george.bluth@reqres.in",
                first_name = "George",
                last_name = "Bluth",
                avatar = "https://reqres.in/img/faces/1-image.jpg"
            )
        } finally {
            isLoading = false
        }
    }

    val followingList = listOf(
        Product("f_1", "Janet Weaver", "Following", "https://reqres.in/img/faces/2-image.jpg", R.drawable.buyimage),
        Product("f_2", "Emma Wong", "Following", "https://reqres.in/img/faces/3-image.jpg", R.drawable.buyimage),
        Product("f_3", "Eve Holt", "Following", "https://reqres.in/img/faces/4-image.jpg", R.drawable.buyimage)
    )
    val pagerState = rememberPagerState(pageCount = { followingList.size })
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.Black
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                AsyncImage(
                    model = userData?.avatar ?: "https://reqres.in/img/faces/1-image.jpg",
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF5F5F5)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.profileimage),
                    error = painterResource(id = R.drawable.profileimage)
                )

                Spacer(modifier = Modifier.height(16.dp))

                val firstName = userData?.first_name ?: "George"
                val lastName = userData?.last_name ?: "Bluth"

                Text(
                    text = "$firstName $lastName",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.width(160.dp).height(45.dp),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    Text(text = "프로필 수정", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val menuItems = listOf(
                        Pair(R.drawable.archive, "주문"),
                        Pair(R.drawable.identificationcard, "패스"),
                        Pair(R.drawable.calendarblank, "이벤트"),
                        Pair(R.drawable.gear, "설정")
                    )

                    menuItems.forEachIndexed { index, item ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                painter = painterResource(id = item.first),
                                contentDescription = item.second,
                                modifier = Modifier.size(24.dp),
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = item.second, fontSize = 12.sp, color = Color.Black)
                        }
                        if (index < menuItems.lastIndex) {
                            VerticalDivider(
                                modifier = Modifier.height(24.dp),
                                color = Color(0xFFEEEEEE)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = Color(0xFFF5F5F5), thickness = 8.dp)

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "나이키 멤버 혜택", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(text = "0개 사용 가능", fontSize = 13.sp, color = Color.Gray)
                    }
                    Icon(painter = painterResource(id = R.drawable.home), contentDescription = null, modifier = Modifier.size(16.dp))
                }

                HorizontalDivider(color = Color(0xFFF5F5F5), thickness = 8.dp)
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "팔로잉 (${followingList.size})", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = "편집", fontSize = 13.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 글자 안 잘리는 110.dp 할당 및 아담한 정사각형 고정 구조 완벽 킵
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth().height(110.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    pageSpacing = 12.dp,
                    pageSize = PageSize.Fixed(100.dp)
                ) { page ->
                    val person = followingList[page]

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFEEEEEE))
                    ) {
                        AsyncImage(
                            model = person.price,
                            contentDescription = "Following Avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier.fillMaxWidth().background(Color(0xFFF9F9F9)).padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "회원 가입일: 2025년 9월", fontSize = 12.sp, color = Color.Gray)
                }
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