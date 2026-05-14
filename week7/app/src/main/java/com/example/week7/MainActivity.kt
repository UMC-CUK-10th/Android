package com.example.week7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.setValue
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.style.TextAlign

sealed class Screen(val route: String, val iconRes: Int, val label: String) {
    object Home : Screen("home", R.drawable.home, "홈")
    object Shop : Screen("shop", R.drawable.buyimage, "구매하기")
    object Wishlist : Screen("wishlist", R.drawable.wishimage, "위시리스트")
    object Bag : Screen("bag", R.drawable.bagimage, "장바구니")
    object Profile : Screen("profile", R.drawable.profileimage, "프로필")
}

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
            composable(Screen.Wishlist.route) {
                WishlistScreen() // 아무런 인자 없이 깔끔하게 호출
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
                // 시스템 아이콘 대신 커스텀 XML 이미지를 사용합니다.
                icon = {
                    Icon(
                        painter = painterResource(id = screen.iconRes),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp) // 아이콘 크기 조절
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

// 2. 개별 화면 구현부 (내용을 채워 나갈 곳)
@Composable
fun HomeScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.White)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp)) // 최상단 상태바 간격

        // 1. 타이틀 영역 (간격 조절)
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

        // 2. 메인 배너 (PNG 이미지)
        Image(
            painter = painterResource(id = R.drawable.homebanner),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 3. What's new 섹션 타이틀
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

        // 4. 가로 스크롤 상품 리스트 (LazyRow 사용)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp), // 시작과 끝 여백
            horizontalArrangement = Arrangement.spacedBy(16.dp) // 아이템 사이 간격
        ) {
            items(5) { // 일단 5개 반복
                ShoeItem()
            }
        }

        Spacer(modifier = Modifier.height(48.dp)) // 하단 네비바와의 간격
    }
}

@Composable
fun ShoeItem() {
    Column(modifier = Modifier.width(280.dp)) { // Figma 비율에 맞춰 너비 조절
        // 상품 이미지 영역 (회색 배경)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f) // 정사각형 유지
                .background(Color(0xFFF6F6F6)) // 연한 회색 배경
        ) {
            // 실제 신발 PNG가 있다면 여기에 Image 추가
            // Image(painter = painterResource(id = R.drawable.shoe1), contentDescription = null)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 상품 정보
        Text(
            text = "Air Jordan XXXVI",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = "US$185",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun ShoeItemCard() {
    Column(
        modifier = Modifier
            .width(160.dp)
            .padding(end = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(160.dp)
                .background(Color(0xFFF5F5F5))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Air Max DN", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(text = "남성 신발", color = Color.Gray, fontSize = 12.sp)
        Text(text = "₩189,000", fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
fun ShopScreen() {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("전체", "Tops & T-Shirts", "Shoes")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // TabRow 대신 ScrollableTabRow를 사용하여 왼쪽으로 정렬합니다.
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.White,
            contentColor = Color.Black,
            edgePadding = 16.dp, // 가장 왼쪽 첫 번째 탭의 시작 여백
            indicator = { tabPositions ->
                // 강조선(Indicator)도 선택된 탭의 길이에 딱 맞게 설정
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    height = 2.dp,
                    color = Color.Black
                )
            },
            divider = { HorizontalDivider(color = Color(0xFFEEEEEE)) }
        ) {
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedTabIndex == index
                Tab(
                    selected = isSelected,
                    onClick = { selectedTabIndex = index },
                    // 텍스트 길이에 맞게 탭 크기가 조절되도록 설정
                    text = {
                        Text(
                            text = title,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.Black else Color.Gray,
                            modifier = Modifier.padding(horizontal = 4.dp) // 탭 간격 미세 조정
                        )
                    }
                )
            }
        }

        // 하단 내용 영역
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "${tabs[selectedTabIndex]} 리스트")
        }
    }
}

@Composable
fun WishlistScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp) // 좌측 여백 24dp
    ) {
        // 상단 상태바 아래로 적절한 간격 (이미지상의 높이 반영)
        Spacer(modifier = Modifier.height(60.dp))

        // 오직 타이틀만 존재
        Text(
            text = "위시리스트",
            fontSize = 32.sp, // 거대한 폰트 크기
            fontWeight = FontWeight.Bold, // 굵게
            color = Color.Black,
            letterSpacing = (-0.5).sp // 나이키 특유의 자간 조절
        )

        // 이 아래는 이미지처럼 완전히 비워둡니다.
    }
}

@Composable
fun ProfileScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "프로필 화면")
    }
}

@Composable
fun BagScreen(onOrderClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 1. 중앙 정렬 영역 (아이콘 + 안내 문구)
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 원형 테두리 안에 장바구니 아이콘
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .border(1.5.dp, Color.Black, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.bagimage), //
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 이미지와 동일한 텍스트 (줄바꿈 포함)
            Text(
                text = "장바구니가 비어 있습니다.\n제품을 추가하면 여기에 표시됩니다.", //
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        // 2. 하단 '주문하기' 버튼
        Button(
            onClick = onOrderClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 32.dp) // 하단 여백 조절
                .height(60.dp)
                .align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(30.dp) // 캡슐 모양
        ) {
            Text(
                text = "주문하기", //
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp
            )
        }
    }
}