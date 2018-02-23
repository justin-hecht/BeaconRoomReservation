package de.iosb.fraunhofer.baeconroomreservation.rest;

import java.util.List;

import de.iosb.fraunhofer.baeconroomreservation.entity.LoginRequest;
import de.iosb.fraunhofer.baeconroomreservation.entity.LoginResponse;
import de.iosb.fraunhofer.baeconroomreservation.entity.NearbyRequest;
import de.iosb.fraunhofer.baeconroomreservation.entity.RoomOverview;
import de.iosb.fraunhofer.baeconroomreservation.entity.QuickRoomReservationRequest;
import de.iosb.fraunhofer.baeconroomreservation.entity.ReservationResponse;
import de.iosb.fraunhofer.baeconroomreservation.entity.ReservationRequest;
import de.iosb.fraunhofer.baeconroomreservation.entity.RoomDetailsRepresentation;
import de.iosb.fraunhofer.baeconroomreservation.entity.SearchRequest;
import de.iosb.fraunhofer.baeconroomreservation.entity.Term;
import de.iosb.fraunhofer.baeconroomreservation.entity.TermDetails;
import de.iosb.fraunhofer.baeconroomreservation.entity.EntityDetailsRepresentation;
import de.iosb.fraunhofer.baeconroomreservation.entity.EntityRepresentation;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * This interface is used for making api calls. It uses retrofit post and get methods.
 *
 * @author Viseslav Sako
 */
interface APIInterface
{
    @POST("/reservation-0.0.1-SNAPSHOT/mobile/login")
    Call<LoginResponse> postLogin(@Body LoginRequest request);

    @POST("/reservation-0.0.1-SNAPSHOT/mobile/room/{id}")
    Call<ReservationResponse> postReservation(@Path("id")String roomId, @Body ReservationRequest request);

    @POST("/reservation-0.0.1-SNAPSHOT/mobile/room/quick/{id}")
    Call<ReservationResponse> postReservation(@Path("id")String roomId, @Body QuickRoomReservationRequest request);

    @POST("/reservation-0.0.1-SNAPSHOT/mobile/nearby")
    Call<List<RoomOverview>> postNerby(@Body NearbyRequest request);

    @GET("/reservation-0.0.1-SNAPSHOT/mobile/users")
    Call<List<EntityRepresentation>> getUsers();

    @GET("/reservation-0.0.1-SNAPSHOT/mobile/room/favorite/{id}")
    Call<ReservationResponse> makeFavorite(@Path("id")String roomId);

    @GET("/reservation-0.0.1-SNAPSHOT/mobile/favorites")
    Call<List<RoomOverview>> getFavorite();

    @GET("/reservation-0.0.1-SNAPSHOT/mobile/favorites/terms")
    Call<List<Term>> getFavoritesTerms();

    @GET("/reservation-0.0.1-SNAPSHOT/mobile/users/terms")
    Call<List<Term>> getMyTerms();

    @POST("/reservation-0.0.1-SNAPSHOT/mobile/favorites/terms/")
    Call<TermDetails> getSelectedTerm(@Body Term termId);

    @POST("/reservation-0.0.1-SNAPSHOT/mobile/users/search")
    Call<List<EntityRepresentation>> postSearchUsers(@Body SearchRequest query);

    @POST("/reservation-0.0.1-SNAPSHOT/mobile/rooms/search")
    Call<List<EntityRepresentation>> postSearchRooms(@Body SearchRequest query);

    @POST("/reservation-0.0.1-SNAPSHOT/mobile/users/one")
    Call<EntityDetailsRepresentation> getUserDetails(@Body SearchRequest query);

    @POST("/reservation-0.0.1-SNAPSHOT/mobile/rooms/one")
    Call<RoomDetailsRepresentation> getRoomDetails(@Body SearchRequest searchRequest);
}
