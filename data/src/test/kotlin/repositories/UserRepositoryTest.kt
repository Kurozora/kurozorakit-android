package repositories

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.data.enums.KKLibrary
import kurozora.kit.data.enums.ReadStatus
import kurozora.kit.data.models.library.LibraryUpdateResponse
import kurozora.kit.data.models.show.attributes.TVRating
import kurozora.kit.data.models.user.User
import kurozora.kit.data.models.user.UserIdentityResponse
import kurozora.kit.data.models.user.UserResponse
import kurozora.kit.data.models.user.tokens.AccessTokenResponse
import kurozora.kit.data.models.user.update.UserUpdate
import kurozora.kit.data.repositories.user.UserRepositoryImpl
import kurozora.kit.shared.KurozoraError
import kurozora.kit.shared.Result
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserRepositoryImplTest {
    private lateinit var mockApiClient: KurozoraApiClient
    private lateinit var userRepository: UserRepositoryImpl

    @BeforeTest
    fun setup() {
        mockApiClient = mockk()
        userRepository = UserRepositoryImpl(mockApiClient)
    }

    @Test
    fun `getMyProfile should return UserResponse on success`() = runBlocking {
        // Arrange
        val expectedResponse = mockk<UserResponse>()
        coEvery { mockApiClient.get<UserResponse>(KKEndpoint.Me.Profile) } returns Result.Success(expectedResponse)

        // Act
        val result = userRepository.getMyProfile()

        // Assert
        assertTrue(result is Result.Success)
        assertEquals(expectedResponse, result.data)
        coVerify { mockApiClient.get<UserResponse>(KKEndpoint.Me.Profile) }
    }

    @Test
    fun `getMyProfile should return Error on failure`() = runBlocking {
        // Arrange
        val exception = Exception("Network error")
        coEvery { mockApiClient.get<UserResponse>(KKEndpoint.Me.Profile) }

        // Act
        val result = userRepository.getMyProfile()

        // Assert
        assertTrue(result is Result.Error)
        assertEquals(exception, result.error)
    }

    @Test
    fun `updateMyProfile should update user and return UserResponse`() = runBlocking {
        // Arrange
        val update = UserUpdate(
            username = "newUsername",
            nickname = "New Nickname",
            biography = "New bio",
            preferredLanguage = "en",
            preferredTVRating = 1,
            preferredTimezone = "UTC"
        )

        val expectedResponse = mockk<UserResponse>()
        coEvery { mockApiClient.post<UserResponse, Unit>(any(), any(), any()) } returns Result.Success(expectedResponse)

        // Act
        val result = userRepository.updateMyProfile(update)

        // Assert
        assertTrue(result is Result.Success)
        assertEquals(expectedResponse, result.data)
        coVerify { mockApiClient.post<UserResponse, Unit>(
            KKEndpoint.Me.Update,
            body = Unit,
            block = any()
        ) }
    }

    @Test
    fun `updateMyProfile should update current user on success`() = runBlocking {
        // Arrange
        val update = UserUpdate(
            username = "newUsername",
            nickname = "New Nickname"
        )

        val mockUser = mockk<User>(relaxed = true)
        val mockResponse = mockk<UserResponse> {
            coEvery { data } returns listOf(mockUser)
        }

        coEvery { mockApiClient.post<UserResponse, Unit>(any(), any(), any()) } returns Result.Success(mockResponse)

        // Act
        userRepository.updateMyProfile(update)

        // Assert
        coVerify { User.current = mockUser }
    }

    @Test
    fun `getMyFollowers should return UserIdentityResponse with parameters`() = runBlocking {
        // Arrange
        val expectedResponse = mockk<UserIdentityResponse>()
        val nextUrl = "https://api.kurozora.app/next-page"
        val limit = 30

        coEvery { mockApiClient.get<UserIdentityResponse>(KKEndpoint.Url(nextUrl), any()) } returns Result.Success(expectedResponse)

        // Act
        val result = userRepository.getMyFollowers(nextUrl, limit)

        // Assert
        assertTrue(result is Result.Success)
        assertEquals(expectedResponse, result.data)
        coVerify { mockApiClient.get<UserIdentityResponse>(
            KKEndpoint.Url(nextUrl),
            mapOf("limit" to limit.toString())
        ) }
    }

    // Diğer test fonksiyonlarını da aynı şekilde runBlocking ile sarmalayıp
    // coEvery ve coVerify kullanarak güncelleyebilirsiniz

    @Test
    fun `getAccessToken should return AccessTokenResponse for given token`() = runBlocking {
        // Arrange
        val token = "12345|abcde"
        val tokenId = "12345"
        val expectedResponse = mockk<AccessTokenResponse>()

        coEvery { mockApiClient.get<AccessTokenResponse>(KKEndpoint.Me.AccessTokens.Details(tokenId)) } returns Result.Success(expectedResponse)

        // Act
        val result = userRepository.getAccessToken(token)

        // Assert
        assertTrue(result is Result.Success)
        assertEquals(expectedResponse, result.data)
        coVerify { mockApiClient.get<AccessTokenResponse>(
            KKEndpoint.Me.AccessTokens.Details(tokenId)
        ) }
    }

    @Test
    fun `updateAccessToken should return Unit on success`() = runBlocking {
        // Arrange
        val token = "12345|abcde"
        val tokenId = "12345"
        val deviceToken = "device123"
        val body = mapOf("apn_device_token" to deviceToken)

        coEvery { mockApiClient.post<Unit, Map<String, String>>(KKEndpoint.Me.AccessTokens.Update(tokenId), body) } returns Result.Success(Unit)

        // Act
        val result = userRepository.updateAccessToken(token, deviceToken)

        // Assert
        assertTrue(result is Result.Success)
        coVerify { mockApiClient.post<Unit, Map<String, String>>(
            KKEndpoint.Me.AccessTokens.Update(tokenId),
            body
        ) }
    }

    @Test
    fun `addToLibrary should return LibraryUpdateResponse`() = runBlocking {
        // Arrange
        val libraryKind = KKLibrary.Kind.LITERATURES
        val libraryStatus = KKLibrary.Status.PLANNING
        val modelId = "model456"
        val body = mapOf(
            "library" to libraryKind.stringValue,
            "status" to libraryStatus.sectionValue,
            "model_id" to modelId
        )
        val expectedResponse = mockk<LibraryUpdateResponse>()

        coEvery { mockApiClient.post<LibraryUpdateResponse, Map<String, String>>(KKEndpoint.Me.Library.Index, body) } returns Result.Success(expectedResponse)

        // Act
        val result = userRepository.addToLibrary(libraryKind, libraryStatus, modelId)

        // Assert
        assertTrue(result is Result.Success)
        assertEquals(expectedResponse, result.data)
        coVerify { mockApiClient.post<LibraryUpdateResponse, Map<String, String>>(
            KKEndpoint.Me.Library.Index,
            body
        ) }
    }

    @Test
    fun `updateMyLibrary should return LibraryUpdateResponse with provided parameters`() = runBlocking {
        // Arrange
        val libraryKind = KKLibrary.Kind.SHOWS
        val modelId = "model789"
        val rewatchCount = 3
        val isHidden = true
        val body = mapOf<String, Any>(
            "library" to libraryKind.stringValue,
            "model_id" to modelId,
            "rewatch_count" to rewatchCount,
            "is_hidden" to isHidden
        )
        val expectedResponse = mockk<LibraryUpdateResponse>()

        coEvery { mockApiClient.post<LibraryUpdateResponse, Map<String, Any?>>(KKEndpoint.Me.Library.Update, body) } returns Result.Success(expectedResponse)

        // Act
        val result = userRepository.updateMyLibrary(libraryKind, modelId, rewatchCount, isHidden)

        // Assert
        assertTrue(result is Result.Success)
        assertEquals(expectedResponse, result.data)
        coVerify { mockApiClient.post<LibraryUpdateResponse, Map<String, Any?>>(
            KKEndpoint.Me.Library.Update,
            body
        ) }
    }

    // Diğer tüm test fonksiyonlarını benzer şekilde güncelleyebilirsiniz
}