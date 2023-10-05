package com.hh2.katj.favorite.service

import com.hh2.katj.util.model.RoadAddress
import com.hh2.katj.favorite.model.entity.Favorite
import com.hh2.katj.favorite.model.dto.RequestAddFavorite
import com.hh2.katj.favorite.repository.FavoriteRepository
import com.hh2.katj.user.model.entity.User
import com.hh2.katj.user.repository.UserRepository
import com.hh2.katj.user.model.entity.Gender
import com.hh2.katj.user.model.entity.UserStatus
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
class FavoriteServiceTest @Autowired constructor(
        private val userRepository: UserRepository,
        private val favoriteRepository: FavoriteRepository,
        private val favoriteService: FavoriteService,
){

    @AfterEach
    fun tearUp() {
        userRepository.deleteAllInBatch()
        favoriteRepository.deleteAllInBatch()
    }

    
    @Test
    fun `사용자가 경로를 즐겨찾기에 추가한다`() {
        // given

        val roadAddress: RoadAddress = RoadAddress(
                addressName = "address_name",
                region1depthName = "r_1",
                region2depthName = "r_2",
                region3depthName = "r_3",
                roadName = "road_name",
                undergroundYn = "Y",
                mainBuildingNo = "1",
                subBuildingNo = "2",
                buildingName = "bn",
                zoneNo = "11232",
                x = "x.123",
                y = "y.321"
        )

        val user: User = User(
                name = "newUser",
                phoneNumber = "123-456-789",
                email = "user@gmail.com",
                gender = Gender.MALE,
                status = UserStatus.ACTIVE,
                roadAddress = roadAddress
        )

        val favorite: Favorite = Favorite(
                user = user,
                roadAddress = roadAddress,
                title = "favorite_title",
                description = "favorite_description"
        )
        
        // when
        val saveUser = userRepository.save(user)
        val requestAddFavorite: RequestAddFavorite = RequestAddFavorite(
                roadAddress = roadAddress,
                favorite.title,
                favorite.description
        )

        // then
        val result: Boolean = favoriteService.addFavorite(saveUser.id, requestAddFavorite)
        Assertions.assertThat(result).isTrue()
    }

    @Test
    fun `사용자가 즐겨찾기를 전부 조회한다`() {
        // given
        val roadAddressA: RoadAddress = RoadAddress(
                addressName = "address_name",
                region1depthName = "r_1",
                region2depthName = "r_2",
                region3depthName = "r_3",
                roadName = "road_name",
                undergroundYn = "Y",
                mainBuildingNo = "1",
                subBuildingNo = "2",
                buildingName = "bn",
                zoneNo = "11232",
                x = "x.123",
                y = "y.321"
        )

        val user: User = User(
                name = "newUser",
                phoneNumber = "123-456-789",
                email = "user@gmail.com",
                gender = Gender.MALE,
                status = UserStatus.ACTIVE,
                roadAddress = roadAddressA
        )


        val roadAddressB: RoadAddress = RoadAddress(
                addressName = "address_name",
                region1depthName = "r_1",
                region2depthName = "r_2",
                region3depthName = "r_3",
                roadName = "road_name",
                undergroundYn = "Y",
                mainBuildingNo = "1",
                subBuildingNo = "2",
                buildingName = "bn",
                zoneNo = "11232",
                x = "x.123",
                y = "y.321"
        )

        val favoriteA: Favorite = Favorite(
                user = user,
                roadAddress = roadAddressA,
                title = "favorite_titleA",
                description = "favorite_descriptionA"
        )

        val favoriteB: Favorite = Favorite(
                user = user,
                roadAddress = roadAddressB,
                title = "favorite_titleB",
                description = "favorite_descriptionB"
        )

        // when
        val saveUser = userRepository.save(user)

        val requestAddFavoriteA: RequestAddFavorite = RequestAddFavorite(
                roadAddress = roadAddressA,
                favoriteA.title,
                favoriteA.description
        )
        val requestAddFavoriteB: RequestAddFavorite = RequestAddFavorite(
                roadAddress = roadAddressB,
                favoriteB.title,
                favoriteB.description
        )
        favoriteService.addFavorite(saveUser.id, requestAddFavoriteA)
        favoriteService.addFavorite(saveUser.id, requestAddFavoriteB)

        val favorites: MutableList<Favorite> = favoriteService.findAllFavorite(saveUser.id)

        // then
        Assertions.assertThat(favorites.size).isEqualTo(2)

    }
    
}