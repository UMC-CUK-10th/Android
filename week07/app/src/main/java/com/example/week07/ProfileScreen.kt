package com.example.week07

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        when (uiState) {
            is ProfileUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Black
                )
            }
            is ProfileUiState.Error -> {
                Text(
                    text = (uiState as ProfileUiState.Error).message,
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Red
                )
            }
            is ProfileUiState.Success -> {
                val data = uiState as ProfileUiState.Success
                ProfileContent(
                    myInfo = data.myInfo,
                    followingList = data.followingList
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileContent(myInfo: ReqresUser, followingList: List<ReqresUser>) {
    val scrollState = rememberScrollState()
    val thickDividerColor = Color(0xFFF5F5F5)
    val lightGrayColor = Color(0xFFE0E0E0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // 👤 [최상단] 프로필 사진 영역
        AsyncImage(
            model = myInfo.avatar,
            contentDescription = "프로필 이미지",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(lightGrayColor)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 이름
        Text(
            text = "${myInfo.first_name} ${myInfo.last_name}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 프로필 수정 버튼 (굴곡형 바)
        OutlinedButton(
            onClick = { /* 프로필 수정 화면 이동 */ },
            shape = RoundedCornerShape(50),
            border = null,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            modifier = Modifier.height(32.dp)
        ) {
            Text(
                text = "프로필 수정",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 1️⃣ 메뉴 아이콘 영역 (주문, 패스, 이벤트, 설정) + 세로 구분선
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GridMenuItem(iconRes = R.drawable.ic_archive, label = "주문")
            VerticalDividerLine()
            GridMenuItem(iconRes = R.drawable.ic_idcard, label = "패스")
            VerticalDividerLine()
            GridMenuItem(iconRes = R.drawable.ic_calendar, label = "이벤트")
            VerticalDividerLine()
            GridMenuItem(iconRes = R.drawable.ic_setting, label = "설정")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Box(modifier = Modifier.fillMaxWidth().height(8.dp).background(thickDividerColor))

        // 2️⃣ 나이키 멤버 혜택 영역
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* 멤버 혜택 클릭 */ }
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "나이키 멤버 혜택",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "0개 사용 가능",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.ic_chevron_right),
                contentDescription = "화살표",
                modifier = Modifier.size(24.dp)
            )
        }

        Box(modifier = Modifier.fillMaxWidth().height(8.dp).background(thickDividerColor))

        // 3️⃣ 팔로잉 목록 영역 (LazyRow 대신 HorizontalPager 장착 완료)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "팔로잉 (${followingList.size})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "편집",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.clickable { /* 편집 기능 */ }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ✨ HorizontalPager 상태 및 속성 정의
            val pagerState = rememberPagerState(pageCount = { followingList.size })

            HorizontalPager(
                state = pagerState,
                pageSize = PageSize.Fixed(65.dp), // 각 아바타 아이템 카드의 가로 너비를 제한하여 한 화면에 여러 명이 나열되도록 구현
                pageSpacing = 20.dp, // 아이템 사이 간격 설정
                contentPadding = PaddingValues(horizontal = 16.dp), // 좌우 시작점 여백 처리
                modifier = Modifier
                    .fillMaxWidth()
                    .height(95.dp)
            ) { page ->
                val user = followingList[page]
                FollowingUserItem(user = user)
            }
        }

        Box(modifier = Modifier.fillMaxWidth().height(8.dp).background(thickDividerColor))

        Spacer(modifier = Modifier.height(32.dp))

        // 4️⃣ 최하단 회원 가입일 텍스트
        Text(
            text = "회원 가입일: 2025년 9월",
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 40.dp)
        )
    }
}

@Composable
fun VerticalDividerLine() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(24.dp)
            .background(Color(0xFFEEEEEE))
    )
}

@Composable
fun GridMenuItem(iconRes: Int, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { }
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}

@Composable
fun FollowingUserItem(user: ReqresUser) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(65.dp)
    ) {
        AsyncImage(
            model = user.avatar,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(65.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0E0E0))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = user.first_name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            maxLines = 1
        )
    }
}